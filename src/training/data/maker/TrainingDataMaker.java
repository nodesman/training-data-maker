/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package training.data.maker;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author raj
 */
public class TrainingDataMaker {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)  {
        
        
        if (args.length < 2)
        {
            showSyntax();
            return;
        }
        File inputFile = new File(args[0]);

        if (!inputFile.exists()) {
            System.out.println("Input file doesn't exist. Please specify a valid input file");
            return;
        }
        
        File outputdir = new File(args[1]);
        if (!outputdir.exists()) {
            System.out.println("Output dir doesn't exist. Please create it");
            return;
        }
        
          FileInputStream stream;
        try {
            stream = new FileInputStream(inputFile);
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open input file.");
            Logger.getLogger(TrainingDataMaker.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
          
          DataInputStream in  = new DataInputStream (stream);
          BufferedReader br = new BufferedReader(new InputStreamReader(in));
          String line = null;
        try {
            while (!((line = br.readLine() ) == null)) {
                String code;
                String url  = line;
                
                HttpClient client = new DefaultHttpClient();
                HttpParams params =  (HttpParams) client.getParams();
                System.out.println("Crawling "+line);
                HttpConnectionParams.setSoTimeout(params, 15000);
                HttpConnectionParams.setConnectionTimeout(params, 15000);


                HttpGet request = new HttpGet();
                try {
                    request.setURI(new URI(url));
                } catch (URISyntaxException ex) {
                    Logger.getLogger(TrainingDataMaker.class.getName()).log(Level.SEVERE, null, ex);
                }
                                    client.getParams().setParameter(HttpMethodParams.USER_AGENT, "Mozilla/5.0 (X11; Linux i686) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.162 Safari/535.19");
                HttpResponse response = null;
                try {
                response = client.execute(request);
                }
                catch (Exception ex) 
                {
                    System.out.println("Error occured");
                    Logger.getLogger(TrainingDataMaker.class.getName()).log(Level.SEVERE, null, ex);
                    continue;
                }


                code = EntityUtils.toString(response.getEntity());

                code =  HTMLStripper.stripHTML(code);
                File outputfile = new File(outputdir.getAbsolutePath()+"/"+getHash(url));
                outputfile.createNewFile();
                FileWriter outputWriter = new FileWriter(outputfile);
                outputWriter.write(code);
                outputWriter.close();
            }
        } catch (IOException ex) {
            System.out.println("Read error while reading input file.");
            Logger.getLogger(TrainingDataMaker.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        
        
    }
     private static String getHash(String url) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            return "";
        }
        messageDigest.reset();
        messageDigest.update(url.getBytes(Charset.forName("UTF8")));
        final byte[] resultByte = messageDigest.digest();
        final String urlhash = new String(Hex.encodeHex(resultByte));
        return urlhash;
    }

    private static void showSyntax() {
        System.out.println("Syntax: java -jar thisjarfile.jar filewithurllist.txt path/to/output/dir");
    }
}
