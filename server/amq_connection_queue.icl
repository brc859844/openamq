<?xml?>
<class
    name      = "amq_connection_queue"
    comment   = "Connection-to-queue management class"
    version   = "1.0"
    script    = "smt_object_gen"
    target    = "smt"
    >
<doc>
This class shows the relationship from connection to queue, via the
consumer object.
</doc>

<inherit class = "smt_object" />
<!-- any containers must come here -->
<inherit class = "amq_console_object" />

<!-- Console definitions for this object -->
<data name = "cml">
    <class name = "connection_queue" parent = "connection" label = "Connection queue">
        <field name = "name">
          <get>icl_shortstr_cpy (field_value, self->queue->name);</get>
        </field>
        <field name = "enabled" label = "Queue accepts new messages?">
          <get>icl_shortstr_fmt (field_value, "%d", self->queue->enabled);</get>
          <put>self->queue->enabled = atoi (field_value);</put>
        </field>
        <field name = "durable" label = "Durable queue?" type = "bool">
          <get>icl_shortstr_fmt (field_value, "%d", self->queue->durable);</get>
        </field>
        <field name = "exclusive" label = "Exclusive to one client?" type = "bool">
          <rule name = "show on summary" />
          <get>icl_shortstr_fmt (field_value, "%d", self->queue->exclusive);</get>
        </field>
        <field name = "exchange_type" label = "Exchange type">
          <rule name = "show on summary" />
          <get>icl_shortstr_cpy (field_value, amq_exchange_type_name (self->queue->last_exchange_type));</get>
        </field>
        <field name = "routing_key" label = "Routing key">
          <rule name = "show on summary" />
          <get>icl_shortstr_cpy (field_value, self->queue->last_routing_key);</get>
        </field>
        <field name = "binding_args" label = "Binding arguments">
          <rule name = "show on summary" />
          <get>icl_shortstr_cpy (field_value, self->queue->last_binding_args);</get>
        </field>
        <field name = "auto_delete" label = "Auto-deleted?" type = "bool">
          <rule name = "show on summary" />
          <get>icl_shortstr_fmt (field_value, "%d", self->queue->auto_delete);</get>
        </field>
        <field name = "consumers" label = "Number of consumers" type = "int">
          <rule name = "show on summary" />
          <get>icl_shortstr_fmt (field_value, "%d", self->queue->consumers);</get>
        </field>
        <field name = "pending" label = "Messages pending" type = "int">
          <rule name = "show on summary" />
          <get>icl_shortstr_fmt (field_value, "%d", amq_queue_message_count (self->queue));</get>
        </field>
        <field name = "messages_in" type = "int" label = "Messages published">
          <rule name = "show on summary" />
          <get>icl_shortstr_fmt (field_value, "%d", self->queue->contents_in);</get>
        </field>
        <field name = "messages_out" type = "int" label = "Messages consumed">
          <rule name = "show on summary" />
          <get>icl_shortstr_fmt (field_value, "%d", self->queue->contents_out);</get>
        </field>
        <field name = "megabytes_in" type = "int" label = "Megabytes published">
          <rule name = "show on summary" />
          <get>icl_shortstr_fmt (field_value, "%d", (int) (self->queue->traffic_in / (1024 * 1024)));</get>
        </field>
        <field name = "megabytes_out" type = "int" label = "Megabytes consumed">
          <rule name = "show on summary" />
          <get>icl_shortstr_fmt (field_value, "%d", (int) (self->queue->traffic_out / (1024 * 1024)));</get>
        </field>
        <field name = "dropped" type = "int" label = "Messages dropped">
          <get>icl_shortstr_fmt (field_value, "%d", self->queue->dropped);</get>
        </field>

        <method name = "purge" label = "Purge all queue messages">
          <exec>amq_queue_basic_purge (self->queue->queue_basic);</exec>
        </method>
    </class>
</data>

<import class = "amq_server_classes" />

<context>
    amq_consumer_t
        *consumer;                          //  Consumer for this relationship
    amq_queue_t
        *queue;                             //  Link to queue
</context>

<!-- The management object has as first argument the management object that
     is its parent.
     Second, whatever objects it has to refer to.
     When we create a new mgt object we also grab links to all objects that
     provide necessary data.  If any of these links fail, we destroy ourselves.
     -->
<method name = "new">
    <argument name = "connection" type = "amq_connection_t *">Parent connection</argument>
    <argument name = "consumer" type = "amq_consumer_t *">Consumer</argument>
    //
    self->consumer = amq_consumer_link (consumer);
    self->queue = amq_queue_link (consumer->queue);
    if (!self->queue)
        self_destroy (&self);
</method>

<method name = "destroy">
    <action>
    amq_consumer_unlink (&self->consumer);
    amq_queue_unlink (&self->queue);
    </action>
</method>

<method name = "selftest" />

</class>
