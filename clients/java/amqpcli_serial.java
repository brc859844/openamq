/*===========================================================================*
 *                                                                           *
 *  amqpcli_serial.java description...                                       *
 *                                                                           *
 *  Written:        5/02/18   Your Name                                      *
 *  Revised:        5/02/18                                                  *
 *                                                                           *
 *  Skeleton generated by LIBERO 2.4 on 18 Feb, 2005, 14:39.                 *
 *===========================================================================*/
import java.awt.*;
import java.applet.*;
import java.net.*;
import java.util.*;
import java.io.*;
import com.imatix.openamq.framing.*;

public class amqpcli_serial extends amqpcli_seriali
{


///////////////////////////   P A R A M E T E R S   ///////////////////////////
// Remote server
String
    opt_server = "localhost";
// Some protocol defaults for this client
AMQConnection.Tune
    client_tune;
AMQConnection.Open
    client_open;
AMQConnection.Close
    client_close;
// Protocol
short
    protocol_port = 7654,
    protocol_id = 128,
    protocol_ver = 1,
    batch_size = 1000;


//////////////////////////////   G L O B A L S   //////////////////////////////
// Network streams
Socket
    amqp = null;
OutputStream
    amqp_out = null;
InputStream
    amqp_in = null;
// Messages
String
    error_message,
    module;
Exception
    exception = null;
// Framing utility
AMQFactory
    mq_factory = null;


///////////////////////////   C O N T R U C T O R   ///////////////////////////

public amqpcli_serial (String args[])
{
    amqpcli_java_execute(args);
}


//////////////////////////////////   M A I N   ////////////////////////////////

public static void main (String args[])
{
    amqpcli_serial
        single = new amqpcli_serial(args);

}

public int amqpcli_java_execute (String args[])
{
    int
        feedback;

    feedback = execute ();
    return (feedback);
}


//////////////////////////   INITIALISE THE PROGRAM   /////////////////////////

public void initialise_the_program ()
{
    the_next_event = ok_event;
}


////////////////////////////   GET EXTERNAL EVENT   ///////////////////////////

public void get_external_event ()
{
}

//%START MODULE

//////////////////////////////////   SETUP   //////////////////////////////////

public void setup ()
{
    try
    {
        // Network setup
        amqp = new Socket(opt_server, protocol_port);
        amqp_in = amqp.getInputStream();
        amqp_out = amqp.getOutputStream();
        mq_factory = new AMQFactory(amqp_in, amqp_out);

        // Client tune capabilities
        client_tune = (AMQConnection.Tune)mq_factory.createFrame(AMQConnection.TUNE);
        client_tune.frameMax = 4000;
        client_tune.channelMax = 128;
        client_tune.handleMax = 128;
        client_tune.heartbeat = 40;
        client_tune.options = null;

        // Client name and connection open defaults
        client_open = (AMQConnection.Open)mq_factory.createFrame(AMQConnection.OPEN);
        client_open.confirmTag = 0;
        client_open.virtualPath = null;
        client_open.clientName = "amqpcli_java (test)";
        client_open.options = null;

        // Connection close defaults
        client_close = (AMQConnection.Close)mq_factory.createFrame(AMQConnection.CLOSE);
        client_close.replyCode = 200;
        client_close.replyText = "amqpcli_serial.java: bye";
    }
    catch (UnknownHostException e)
    {
        raise_exception(exception_event, e, "amqpci_java", "setup", "unknown host");
    }
    catch (IOException e)
    {
        raise_exception(exception_event, e, "amqpci_java", "setup", "IOException");
    }
    catch (AMQFramingException e) {}

    the_next_event = send_connection_initiation_event;
}


/////////////////////////////   FORCED SHUTDOWN   /////////////////////////////

public void forced_shutdown ()
{
    // Close connection
    try
    {
        if (amqp_in != null)
            amqp_in.close();
        if (amqp_out != null)
            amqp_out.close();

        if (exception != null)
            AMQFactory.error(exception, "amqpcli_java", module, error_message);
    }
    catch (IOException e) {}
    catch (AMQFramingException e) {}
}


//////////////////////////   TERMINATE THE PROGRAM   //////////////////////////

public void terminate_the_program ()
{
    System.out.println("amqpcli_java terminating.");
    System.exit(exception != null ? 1 : 0);
}


////////////////////////   SEND CONNECTION INITIATION   ///////////////////////

public void send_connection_initiation ()
{
    try
    {
        // Send initiation
        amqp_out.write(protocol_id);
        amqp_out.write(protocol_ver);
    }
    catch (IOException e)
    {
        raise_exception(exception_event, e, "amqpci_java", "send_connection_initiation", "unable to connect");
    }

    the_next_event = connection_challenge_event;
}


///////////////////////////   SEND CONNECTION OPEN   //////////////////////////

public void send_connection_open ()
{
    try
    {
        client_open.virtualPath = "/test";
        mq_factory.produceFrame(client_open);
    }
    catch (AMQFramingException e)
    {
        raise_exception(exception_event, e, "amqpci_java", "send_connection_open", "cannot open connection");
    }

    the_next_event = do_tests_event;
}


/////////////////////////////////   DO TESTS   ////////////////////////////////

public void do_tests ()
{
    try
    {
        // Channel
        AMQChannel.Open
            channel_open = (AMQChannel.Open)mq_factory.createFrame(AMQChannel.OPEN);
        AMQChannel.Commit
            channel_commit = (AMQChannel.Commit)mq_factory.createFrame(AMQChannel.COMMIT);
        AMQChannel.Ack
            channel_ack = (AMQChannel.Ack)mq_factory.createFrame(AMQChannel.ACK);
        AMQChannel.Close
            channel_close = (AMQChannel.Close)mq_factory.createFrame(AMQChannel.CLOSE);
        // Handle
        AMQHandle.Open
            handle_open = (AMQHandle.Open)mq_factory.createFrame(AMQHandle.OPEN);
        AMQHandle.Send
            handle_send = (AMQHandle.Send)mq_factory.createFrame(AMQHandle.SEND);
        AMQHandle.Consume
            handle_consume = (AMQHandle.Consume)mq_factory.createFrame(AMQHandle.CONSUME);
        AMQHandle.Flow
            handle_flow = (AMQHandle.Flow)mq_factory.createFrame(AMQHandle.FLOW);
        AMQHandle.Notify
            handle_notify = null;
        AMQHandle.Created
            handle_created;
        // Message
        AMQMessage.Head
            message_head = (AMQMessage.Head)mq_factory.createMessageHead();
        byte[]
            message_body;

        // Open channel
        channel_open.channelId = 1;
        channel_open.confirmTag = 0;
        channel_open.transacted = true;
        channel_open.restartable = false;
        channel_open.options = null;
        channel_open.outOfBand = "";
        mq_factory.produceFrame(channel_open);

        // Open hadle
        handle_open.channelId = 1;
        handle_open.handleId = 1;
        handle_open.serviceType = 1;
        handle_open.confirmTag = 0;
        handle_open.producer = true;
        handle_open.consumer = true;
        handle_open.browser = false;
        handle_open.temporary = true;
        handle_open.destName = "";
        handle_open.mimeType = "";
        handle_open.encoding = "";
        handle_open.options = null;
        mq_factory.produceFrame(handle_open);
        // Get handle created
        handle_created = (AMQHandle.Created)mq_factory.consumeFrame();

        // Pause incoming messages
        handle_flow.handleId = 1;
        handle_flow.confirmTag = 0;
        handle_flow.flowPause = true;
        mq_factory.produceFrame(handle_flow);

        // Prepare commit and ack
        channel_commit.channelId = 1;
        channel_commit.confirmTag = 0;
        channel_commit.options = null;
        channel_ack.channelId = 1;
        channel_ack.confirmTag = 0;
        channel_ack.messageNbr = 0;

        // Send handles and growing messages; commit on the fly
        handle_send.handleId = 1;
        handle_send.confirmTag = 0;
        handle_send.fragmentSize = 0;
        handle_send.partial = false;
        handle_send.outOfBand = false;
        handle_send.recovery = false;
        handle_send.streaming = false;
        handle_send.destName = "";
        message_head.bodySize = 0;
        message_head.persistent = false;
        message_head.priority = 1;
        message_head.expiration = 0;
        message_head.mimeType = "";
        message_head.encoding = "";
        message_head.identifier = "";
        message_head.headers = null;
        System.out.println("Sending " + client_tune.frameMax + " messages to server...");
        int i = 1;
        for (; true; i++) {
            // Create the message body
            message_head.bodySize = i;
            message_body = new byte[i];
            body_fill(message_body, i);
            // Set the fragment size
            handle_send.fragmentSize = message_head.encode() + message_head.bodySize;
            if (handle_send.fragmentSize > client_tune.frameMax)
                break;
            // Send message
            mq_factory.produceFrame(handle_send);
            mq_factory.produceMessageHead(message_head);
            mq_factory.produceData(message_body);
            // Commit from time to time
            if (i % batch_size == 0) {
                mq_factory.produceFrame(channel_commit);
                System.out.println("Commit batch " + (i / batch_size) + "...");
            }
        }
        // Commit leftovers
        mq_factory.produceFrame(channel_commit);

        // Resume incoming messages
        handle_flow.flowPause = false;
        mq_factory.produceFrame(handle_flow);

        // Read back
        handle_consume.handleId = 1;
        handle_consume.confirmTag = 0;
        handle_consume.prefetch = batch_size;
        handle_consume.noLocal = false;
        handle_consume.unreliable = false;
        handle_consume.destName = "";
        handle_consume.identifier = "";
        handle_consume.selector = null;
        handle_consume.mimeType = "";
        // Request consume messages
        mq_factory.produceFrame(handle_consume);
        System.out.println("Reading messages back from the server...");
        int j = 1;
        for (i--; i > 0; i--, j++) {
            // Get handle notify
            handle_notify = (AMQHandle.Notify)mq_factory.consumeFrame();
            message_head = mq_factory.consumeMessageHead();
            body_check(mq_factory.consumeData(message_head.bodySize), j);
            // Acknowledge & commit from time to time
            if (j % batch_size == 0) {
                channel_ack.messageNbr = handle_notify.messageNbr;
                mq_factory.produceFrame(channel_ack);
                mq_factory.produceFrame(channel_commit);
                System.out.println("Acknowledge batch " + (j / batch_size) + "...");
            }
        }
        // Acknowledge & commit leftovers
        channel_ack.messageNbr = handle_notify.messageNbr;
        mq_factory.produceFrame(channel_ack);
        mq_factory.produceFrame(channel_commit);

        // Say bye
        channel_close.channelId = 1;
        channel_close.replyCode = 200;
        channel_close.replyText = "amqpcli_serial.java: I'll be back";
        mq_factory.produceFrame(channel_close);
        channel_close = (AMQChannel.Close)mq_factory.consumeFrame();
        mq_factory.produceFrame(client_close);
        client_close = (AMQConnection.Close)mq_factory.consumeFrame();
        System.out.println("Closing, server says: " + client_close.replyText + ".");
    }
    catch (ClassCastException e)
    {
        raise_exception(exception_event, e, "amqpci_java", "do_tests", "unexpected frame from server");
    }
    catch (IOException e)
    {
        raise_exception(exception_event, e, "amqpci_java", "do_tests", "IOException");
    }
    catch (AMQFramingException e)
    {
        raise_exception(exception_event, e, "amqpci_java", "do_tests", "framing error");
    }

    the_next_event = done_event;
}



/////////////////////////   GET CONNECTION CHALLENGE   ////////////////////////

public void face_connection_challenge ()
{
    try
    {
        AMQConnection.Challenge
            // Get the challenge
            challenge = (AMQConnection.Challenge)mq_factory.consumeFrame();
        AMQConnection.Response
            // New response
            response = (AMQConnection.Response)mq_factory.createFrame(AMQConnection.RESPONSE);
        // Send the response
        response.mechanism = "plain";
        response.responses = null;
        mq_factory.produceFrame(response);
    }
    catch (ClassCastException e)
    {
        raise_exception(exception_event, e, "amqpci_java", "face_connection_challenge", "unexpected frame from server");
    }
    catch (AMQFramingException e)
    {
        raise_exception(exception_event, e, "amqpci_java", "face_connection_challenge", "authentication error");
    }

    the_next_event = connection_tune_event;
}


////////////////////////   NEGOTIATE CONNECTION TUNE   ////////////////////////

public void negotiate_connection_tune ()
{
    try
    {
        AMQConnection.Tune
            // Get tune parameters from server
            tune_server = (AMQConnection.Tune)mq_factory.consumeFrame();

        // Send the reply
        client_tune.frameMax = (short)Math.min(client_tune.frameMax, tune_server.frameMax);
        client_tune.channelMax = (short)Math.min(client_tune.channelMax, tune_server.channelMax);
        client_tune.handleMax = (short)Math.min(client_tune.handleMax, tune_server.handleMax);
        client_tune.heartbeat = (short)Math.min(client_tune.heartbeat, tune_server.heartbeat);
        mq_factory.produceFrame(client_tune);
    }
    catch (ClassCastException e)
    {
        raise_exception(exception_event, e, "amqpci_java", "negotiate_connection_tune", "unexpected frame from server");
    }
    catch (AMQFramingException e)
    {
        raise_exception(exception_event, e, "amqpci_java", "negotiate_connection_tune", "tune error");
    }
}


/////////////////////////////////   SHUTDOWN   ////////////////////////////////

public void shutdown ()
{
}


//- Standard dialog routines --------------------------------------------
public void raise_exception (int event, Exception e, String _class, String module, String message)
{
    this.error_message = message;
    this.module = module;
    this.exception = e;

    System.err.println(e.getMessage());
    System.err.println(_class + ": " + module + ": " + message + ".");

    // Reset message
    error_message = null;
    module = null;
    exception = null;

    raise_exception (event);
}

//- Auxiliary routines --------------------------------------------
byte[] body_fill(byte[] body, int seed) {
    for (int i = 0; i < body.length; i++) {
        if (i == 0)
            body[i] = (byte)(seed % Byte.MAX_VALUE);
        else
            body[i] = (byte)((body[i - 1] * body[i - 1]) % Byte.MAX_VALUE);
    }

    return body;
}

void body_check(byte[] body, int seed) {
    byte[] ref = new byte[body.length];

    ref = body_fill(ref, seed);
    for (int i = 0; i < body.length; i++) {
        if (body[i] != ref[i]) {
            System.err.println("amqpcli_serial: body_check: returning message mismatch.");
            System.exit(1);
        }
    }
}

//%END MODULE
}
