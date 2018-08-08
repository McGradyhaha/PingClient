import java.net.*;


public class PingClient
{

    public static void main(String[] args) throws Exception
    {
        // declare min, max and average rtt.
        long total = 0;
        long min = 1000;
        long max = 0;
        int sucCount = 0;

        // Requires host and port number.
        if (args.length != 2)
        {
            System.out.println("You need to inform host and port. (PingClient host port)");
            return;
        } else {
            System.out.println("\nHost: "+ args[0] + "\nPort number: "+args[1]+"\n");
        }

        // Declare host and portNumber
        InetAddress host = InetAddress.getByName(args[0]);
        int portNumber = Integer.parseInt(args[1]);

        // Create a datagram socket used for sending and recieving packets, and // set timeout to 1000ms
        DatagramSocket socket = new DatagramSocket();
        socket.setSoTimeout(1000);

        int loop = 10;
        // Start loop to send packets
        for (int i = 0; i < loop; i++)
        {
            // Create ping message
            long sent_time = System.currentTimeMillis();
            String ping_message = pingMessage(i);

            // Create send packet
            DatagramPacket send_packet =
                    new DatagramPacket(ping_message.getBytes(), ping_message.length(), host, portNumber);

            //Send ping request
            socket.send(send_packet);

            //Datagram packet for the server response
            DatagramPacket receive_packet =
                    new DatagramPacket(new byte[1024], 1024);

            //Wait for ping response
            try
            {
                // Response received
                socket.receive(receive_packet);
                long received_time = System.currentTimeMillis();
                long rtt = received_time - sent_time;
                System.out.println("ping to "+
                        receive_packet.getAddress().getHostAddress() + ping_message +", rtt ="
                        + rtt + "ms");
                sucCount++;
                total += rtt;
                if(rtt > max){
                    max = rtt;
                }
                if(rtt < min){
                    min = rtt;
                }
            }
            // Catch timeout exception
            catch (SocketTimeoutException ste)
            {
                System.out.println("ping to "+
                        args[0] + ping_message + ", Timeout");
            }
            // Catch other exceptions
            catch (Exception e)
            {
                System.out.println(e.getMessage());
                return;
            }
        }
        System.out.println("\nrtt summary: "+ "\nsuccess: " + sucCount + "\ntimeout: " + (loop-sucCount) +
                "\nMax: " + max + "ms\nMax: " + min + "ms\nAverage:" + total/sucCount + "ms");
    }

    private static String pingMessage(int i)
    {
        return ", seq = " + i;
    }

}