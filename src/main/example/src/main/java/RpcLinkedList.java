package dev.treset.servermanagementextender;

import dev.treset.servermanagementextender.wrapper.ManagementSchema;
import dev.treset.servermanagementextender.wrapper.ServerManagementInitialized;

import java.util.Optional;

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
