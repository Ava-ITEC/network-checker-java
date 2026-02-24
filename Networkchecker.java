import java.net.*;
import java.io.*;
import java.util.Scanner;

public class NetworkChecker {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Network Checker Tool ===");
        System.out.print("Enter host (default: google.com): ");
        String host = sc.nextLine().trim();
        if (host.isEmpty()) host = "google.com";

        System.out.println("\n--- DNS Resolve ---");
        dnsResolve(host);

        System.out.println("\n--- Ping Test ---");
        ping(host);

        System.out.println("\n--- HTTP Test ---");
        httpCheck(host);

        System.out.println("\nDone.");
    }

    private static void dnsResolve(String host) {
        try {
            InetAddress[] addrs = InetAddress.getAllByName(host);
            for (InetAddress a : addrs) {
                System.out.println("Resolved: " + a.getHostAddress());
            }
        } catch (Exception e) {
            System.out.println("DNS resolve failed: " + e.getMessage());
        }
    }

    private static void ping(String host) {
        try {
            InetAddress addr = InetAddress.getByName(host);
            long start = System.currentTimeMillis();
            boolean ok = addr.isReachable(3000);
            long ms = System.currentTimeMillis() - start;

            if (ok) System.out.println("Ping OK (" + ms + " ms)");
            else System.out.println("Ping FAILED (timeout)");
        } catch (Exception e) {
            System.out.println("Ping error: " + e.getMessage());
        }
    }

    private static void httpCheck(String host) {
        String[] urls = { "https://" + host, "http://" + host };

        for (String url : urls) {
            try {
                HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
                con.setConnectTimeout(3000);
                con.setReadTimeout(3000);
                con.setRequestMethod("GET");

                int code = con.getResponseCode();
                System.out.println(url + " -> HTTP " + code);
                return;
            } catch (Exception ignored) {
                // try next
            }
        }
        System.out.println("HTTP check failed (both https and http).");
    }
}