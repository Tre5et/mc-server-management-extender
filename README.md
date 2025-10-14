# MC Server Management Extender Library
This library provides an interface to more easily extend the server management protocol introduced in 1.21.9.

## Features
- Creating custom Server Management Data schemas
- Creating custom notification methods
- Creating custom request methods

## Usage
> All relevant methods are documented using JavaDoc.

### Registering custom data
All data must be initialized statically in classes marked with the `@ServerManagementInitialized` annotation. 
In these classes data may be initialized in `static` variables / constants or the `static` constructor block.

This guarantees that registration takes place at the correct time in the server lifecycle, which is usually before the mod initializer is called.
Using features of this library at any other time will likely cause unexpected behaviour and crashes.

### Creating custom Server Management Data schemas
Data schema are used to define what kind of data is sent or received by the server management methods.

The class `ManagementSchema` holds this schema. 
It provides some default schemas for datatypes used in the game, accessible by `ManagementSchema.[DATATYPE]`. 

Further it provides the method `ManagementSchema.builder(String namespace, String name)` to create a schema builder.
The `namespace` and `name` parameters create a unique identifier for the schema.

It is often necessary to specify the datatype represented by this schema as a type parameter like `ManagementSchema.<DataType>builder(...)`.

#### Using the schema builder

Each method of the schema builder will return a new schema builder that must be used for further modification.

The schema builder provides the methods `property(String name, ManagementSchema schema, Function getter)` and `optionalProperty(String name, ManagementSchema schema, Function getter)`.

These can be used to add a property to the schema. Optional properties are not required to be set when receiving a request.

The parameters are used as follows:
- `String name`: The name of the property.
- `ManagementSchema schema`: The schema of data in this property.
- `Function getter`: A function getting a object of the data type represented by the property from the object that the schema represents.

Further the method `build(Function applicator)` is provided. This builds and registers the schema and returns a `ManagementSchema`. The parameter applicator is a function to construct a new object of the data type represented by the schema from the parameter types.

#### Example:

```java
@ServerManagementInitialized
public record RpcChatMessage(
        RpcPlayer player,
        String message
) {
    public static final ManagementSchema<RpcChatMessage> SCHEMA = ManagementSchema
            .<RpcChatMessage>builder("your_mod_id", "chat_message")
            .property("player", ManagementSchema.PLAYER, RpcChatMessage::player)
            .property("message", ManagementSchema.STRING, RpcChatMessage::message)
            .build(RpcChatMessage::new);
}
```

#### List properties

The class `ManagementSchema` provides the method `asList()` that returns a `ManagementSchema` representing a list of the schema it was called on. This can then be used as normal in the builder.

Example:
```java
@ServerManagementInitialized
public record RpcPlayerList(
        int playerCount,
        List<RpcPlayer> players
) {
    public static final ManagementSchema<RpcPlayerList> SCHEMA = ManagementSchema
            .<RpcPlayerList>builder("your_mod_id", "player_list")
            .property("count", ManagementSchema.INTEGER, RpcPlayerList::playerCount)
            .property("players", ManagementSchema.PLAYER.asList(), RpcPlayerList::players)
            .build(RpcPlayerList::new);
}

```

#### Recursive schemas

For some schemas it may be required to use itself recursively. To enable this the method `ManagementSchema.recursive(String namespace, String name, Function builder)` is provided.

The `namespace` and `name` parameters work exactly like in the method `builder(...)`. The `builder` parameter is a function that takes a schema builder and a `ManagementSchema` as arguments and returns a new `ManagementSchema`. The `ManagementSchema` argument can then be used as the schema that is created.

Example:
```java
@ServerManagementInitialized
public record RpcLinkedList(
        String value,
        Optional<RpcLinkedList> next
) {
    public static final ManagementSchema<RpcLinkedList> SCHEMA = ManagementSchema
            .recursive("your_mod_id", "linked_list",
                    (builder, self) -> builder
                            .property("value", ManagementSchema.STRING, RpcLinkedList::value)
                            .optionalProperty("next", self, RpcLinkedList::next)
                            .build(RpcLinkedList::new)
            );
}
```

### Creating custom notification methods

Notification methods are represented by a `RpcNotificationHandler`.
This provides a method `send([DataType] data)` that can be used to send the notification to all connected clients.

Notification handlers can be built using a builder provided by `RpcNotificationHandler.builder(ManagementSchema schema)`.
The schema is the data schema sent by the notification.

This builder provides three configuration methods:
- `identifier(String namespace, String path)`: Required to be set before build. The unique identifier of the notification type.
- `description(String description)`: The description of the notification type.
- `propertyName(String name)`: The name of the property in the notification. Only relevant for displaying the schema.

After configuration the handler can be built using the method `build()` which returns a `RpcNotificationHandler`.

#### Example:

```java
@ServerManagementInitialized
public class ChatMessageNotification {
    public static final RpcNotificationHandler<RpcChatMessage> HANDLER = RpcNotificationHandler
            .builder(RpcChatMessage.SCHEMA)
            .description("Chat message was sent")
            .identifier("your_mod_id", "notification/chat_message")
            .build();
}
```

### Creating custom request methods

Request methods are methods that can be called by a server management client and have a response generated by the server.

They can be created using the `RpcMethodBuilder.of(ManagementSchema schema)` method.
The schema represents the data type of the response.

This builder provides the methods `identifier(String namespace, String path)` and `description(String description)` identical to the notification builder.

Further methods are provided:
- `responsePropertyName(String name)`: The name of the property in the response. Only relevant for displaying the schema.
- `parameter(ManagementSchema schema)`: Adds a parameter requirement to the request. The schema is the expected data type in the request parameter.
- `parameterName(String name)`: Sets the name of the parameter in the request. Only available if `parameter(...)` was called previously.

Further the method `build(Function handler)` is provided.
This builds the method. The `handler` is the function that is called when a request is received. It differs whether the method expects a parameter or not.
- No parameter: Function taking a `ManagementHandlerDispatcher` as a parameter and returning an object of the response type.
- Parameter: Function taking a `ManagementHandlerDispatcher`, an object of the request type and a `ManagementConnectionId` and returning an object of the response type.

#### Example:

```java
@ServerManagementInitialized
public class ChatMessageMethod {
    static {
        RpcMethodBuilder.of(ManagementSchema.BOOLEAN)
                .responsePropertyName("success")
                .parameter(RpcChatMessage.SCHEMA)
                .parameterName("message")
                .description("Send a message on behalf of a player")
                .identifier("your_mod_id", "chat_message/send")
                .build(ChatMessageMethod::chatMessageHandler);
    }
    
    private static boolean chatMessageHandler(ManagementHandlerDispatcher d, RpcChatMessage message, ManagementConnectionId i) {
        // Your handler code here
    }
}
```

### For more custom setups

For every method that takes a `ManagementSchema` there also exists an equivalent method that takes a `Codec` and a `RpcSchema`. 

Similarly `MessageSchema` can be created from a `Codec` and a `RpcSchema` by using the constructor.

It's important to pay attention that codec and schema match when using these methods.

