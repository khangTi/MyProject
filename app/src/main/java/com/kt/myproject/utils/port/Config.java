package com.kt.myproject.utils.port;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * -------------------------------------------------------------------------------------------------
 *
 * @Project: no name
 * @Created: KOP 2021/07/12
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
public class Config {

    public static int socketCount = 0;

    /**
     * The static <code>Properties</code>.
     */

    private static Properties prp = new Properties();
    public static String DEFAULT_OEM_ENCODING = "Cp850";

    static {
        String filename;
        int level;
        FileInputStream in = null;

        try {
            filename = System.getProperty( "jcifs.properties" );
            if( filename != null && filename.length() > 1 ) {
                in = new FileInputStream( filename );
            }
            Config.load( in );
            if (in != null)
                in.close();
        } catch( IOException ioe ) {

        }


        try {
            "".getBytes(DEFAULT_OEM_ENCODING);
        } catch (UnsupportedEncodingException uee) {

            DEFAULT_OEM_ENCODING = "US-ASCII";
        }

    }

    /**
     * This static method registers the SMB URL protocol handler which is
     * required to use SMB URLs with the <tt>java.net.URL</tt> class. If this
     * method is not called before attempting to create an SMB URL with the
     * URL class the following exception will occur:
     * <blockquote><pre>
     * Exception MalformedURLException: unknown protocol: smb
     *     at java.net.URL.<init>(URL.java:480)
     *     at java.net.URL.<init>(URL.java:376)
     *     at java.net.URL.<init>(URL.java:330)
     *     at jcifs.smb.SmbFile.<init>(SmbFile.java:355)
     *     ...
     * </pre><blockquote>
     */

    public static void registerSmbURLHandler() {
        String ver, pkgs;

        ver = System.getProperty( "java.version" );
        if( ver.startsWith( "1.1." ) || ver.startsWith( "1.2." )) {
            throw new RuntimeException( "jcifs-0.7.0b4+ requires Java 1.3 or above. You are running " + ver );
        }
        pkgs = System.getProperty( "java.protocol.handler.pkgs" );
        if( pkgs == null ) {
            System.setProperty( "java.protocol.handler.pkgs", "jcifs" );
        } else if( pkgs.indexOf( "jcifs" ) == -1 ) {
            pkgs += "|jcifs";
            System.setProperty( "java.protocol.handler.pkgs", pkgs );
        }
    }

    // supress javadoc constructor summary by removing 'protected'
    Config() {}

    /**
     * Set the default properties of the static Properties used by <tt>Config</tt>. This permits
     * a different Properties object/file to be used as the source of properties for
     * use by the jCIFS library. The Properties must be set <i>before jCIFS
     * classes are accessed</i> as most jCIFS classes load properties statically once.
     * Using this method will also override properties loaded
     * using the <tt>-Djcifs.properties=</tt> commandline parameter.
     */

    public static void setProperties( Properties prp ) {
        Config.prp = new Properties( prp );
        try {
            Config.prp.putAll( System.getProperties() );
        } catch( SecurityException se ) {

        }
    }

    /**
     * Load the <code>Config</code> with properties from the stream
     * <code>in</code> from a <code>Properties</code> file.
     */

    public static void load( InputStream in ) throws IOException {
        if( in != null ) {
            prp.load( in );
        }
        try {
            prp.putAll( System.getProperties() );
        } catch( SecurityException se ) {

        }
    }

    public static void store(OutputStream out, String header ) throws IOException {
        prp.store( out, header );
    }

    /**
     * List the properties in the <code>Code</code>.
     */

    public static void list( PrintStream out ) throws IOException {
        prp.list( out );
    }

    /**
     * Add a property.
     */

    public static Object setProperty( String key, String value ) {
        return prp.setProperty( key, value );
    }

    /**
     * Retrieve a property as an <code>Object</code>.
     */

    public static Object get( String key ) {
        return prp.get( key );
    }

    /**
     * Retrieve a <code>String</code>. If the key cannot be found,
     * the provided <code>def</code> default parameter will be returned.
     */

    public static String getProperty( String key, String def ) {
        return prp.getProperty( key, def );
    }

    /**
     * Retrieve a <code>String</code>. If the property is not found, <code>null</code> is returned.
     */

    public static String getProperty( String key ) {
        return prp.getProperty( key );
    }

    /**
     * Retrieve an <code>int</code>. If the key does not exist or
     * cannot be converted to an <code>int</code>, the provided default
     * argument will be returned.
     */

    public static int getInt( String key, int def ) {
        String s = prp.getProperty( key );
        if( s != null ) {
            try {
                def = Integer.parseInt( s );
            } catch( NumberFormatException nfe ) {

            }
        }
        return def;
    }

    /**
     * Retrieve an <code>int</code>. If the property is not found, <code>-1</code> is returned.
     */

    public static int getInt( String key ) {
        String s = prp.getProperty( key );
        int result = -1;
        if( s != null ) {
            try {
                result = Integer.parseInt( s );
            } catch( NumberFormatException nfe ) {

            }
        }
        return result;
    }

    /**
     * Retrieve a <code>long</code>. If the key does not exist or
     * cannot be converted to a <code>long</code>, the provided default
     * argument will be returned.
     */

    public static long getLong( String key, long def ) {
        String s = prp.getProperty( key );
        if( s != null ) {
            try {
                def = Long.parseLong( s );
            } catch( NumberFormatException nfe ) {

            }
        }
        return def;
    }

    /**
     * Retrieve an <code>InetAddress</code>. If the address is not
     * an IP address and cannot be resolved <code>null</code> will
     * be returned.
     */

    public static InetAddress getInetAddress(String key, InetAddress def ) {
        String addr = prp.getProperty( key );
        if( addr != null ) {
            try {
                def = InetAddress.getByName( addr );
            } catch( UnknownHostException uhe ) {

            }
        }
        return def;
    }
    public static InetAddress getLocalHost() {
        String addr = prp.getProperty( "jcifs.smb.client.laddr" );

        if (addr != null) {
            try {
                return InetAddress.getByName( addr );
            } catch( UnknownHostException uhe ) {
            }
        }

        return null;
    }

    /**
     * Retrieve a boolean value. If the property is not found, the value of <code>def</code> is returned.
     */

    public static boolean getBoolean( String key, boolean def ) {
        String b = getProperty( key );
        if( b != null ) {
            def = b.toLowerCase().equals( "true" );
        }
        return def;
    }

    /**
     * Retrieve an array of <tt>InetAddress</tt> created from a property
     * value containting a <tt>delim</tt> separated list of hostnames and/or
     * ipaddresses.
     */

    public static InetAddress[] getInetAddressArray( String key, String delim, InetAddress[] def ) {
        String p = getProperty( key );
        if( p != null ) {
            StringTokenizer tok = new StringTokenizer( p, delim );
            int len = tok.countTokens();
            InetAddress[] arr = new InetAddress[len];
            for( int i = 0; i < len; i++ ) {
                String addr = tok.nextToken();
                try {
                    arr[i] = InetAddress.getByName( addr );
                } catch( UnknownHostException uhe ) {
                    return def;
                }
            }
            return arr;
        }
        return def;
    }
}
