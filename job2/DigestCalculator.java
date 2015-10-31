/*
 * PUC-Rio - Pontificia Universidade Catolica
 *
 * Seguranca da Informcao 2010.2
 * Professor: Anderson O. da Silva
 *
 * Nome: Luiz Felipe Machado
 *       Henrique Taunay
 */

import java.io.*;
import java.util.*;
import java.security.*;

/**
 *
 * @author htaunay
 */
public class DigestCalculator {

    public enum STATUS {

        OK, NOTOK, NOTFOUND
    }
    
    public static byte[] readFile(File file) throws IOException
    {
        InputStream is = new FileInputStream(file);
    
        // Get the size of the file
        long length = file.length();
    
        if (length > Integer.MAX_VALUE) {
            return null;
        }
    
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];
    
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
    
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }
    
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

    public static String calcHex(byte[] bytes) {
    	
        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0x0100 + (bytes[i] & 0x00FF)).substring(1);
            buf.append(hex.length() < 2 ? "0" : "").append(hex);
        }

        return buf.toString();
    }

    public static void writeToEOF(String s, File f) {
    	
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
            bw.write(s);
            bw.close();

        } catch (IOException e) {
            System.out.println("Problems writing to file!" +
                    e.getStackTrace().toString());
        }
    }

    public static int find(Vector<String> fileNames, String name,
            Vector<String> fileTypes, String type) {

        int pos = -1;
        // necessario procurar um por um porque pode ter arquivos
        // com o mesmo nome mas com tipo diferentes
        for (int i = 0; i < fileNames.size(); i++) {
            if (fileNames.get(i).equals(name) && fileTypes.get(i).equals(type)) {
                pos = i;
            }
        }
        return pos;
    }

    public static void main(String[] args) throws Exception {

    	String digestType = args[0];
    	String digestFile = args[1];
  
        Vector<String> digestListFiles = new Vector<String>();
        Vector<byte[]> calcDigests = new Vector<byte[]>();

        BufferedReader reader = null;
        byte[] fileContent;
        int i;

        for( i = 2; i < args.length; i++ )
    	{
    		digestListFiles.add( args[i] );
    	}

        // le os arquivos e calcula o digest de seus conteudos.
        for (i = 0; i < digestListFiles.size(); i++) {
        	
            File file = new File( digestListFiles.get(i) );

            fileContent = readFile( file );
            MessageDigest messageDigest;
            
            try
            {	
            	messageDigest = MessageDigest.getInstance(digestType);
            }
            catch( Exception e)
            {
            	System.out.println("Inexistant cryptography algorithm given!" );
            	return;
            }
            
            messageDigest.update(fileContent);
            calcDigests.add(messageDigest.digest());
        }

        File digestListFile = new File(digestFile);

        // verifica se o arquivo existe.
        // caso nao exista, cria um novo.
        if( !digestListFile.exists() )
            digestListFile.createNewFile();

        // lista dos arquivos ja lidos
        Vector<String> fileNames = new Vector<String>();
        // lista do tipos dos digest dos arquivos
        Vector<String> fileTypes = new Vector<String>();
        // lista dos digest dos conteudos dos arquivos
        Vector<String> fileDigests = new Vector<String>();

        try {
            reader = new BufferedReader(new FileReader(digestListFile));
            String line;
            String[] splitLine;

            while ((line = reader.readLine()) != null) {
                splitLine = line.split(" ");
                fileNames.add(splitLine[0]); // le o nome do arquivo
                fileTypes.add(splitLine[1]); // le o tipo do digest
                fileDigests.add(splitLine[2]); // le o digest em hex
            }

        } catch (IOException e) {
            System.out.println("Problems reading digest file! "
                    + e.getStackTrace().toString());
        }

        STATUS status;
        int pos;
        for (i = 0; i < digestListFiles.size(); i++) {

            // procura
            pos = find(fileNames, digestListFiles.get(i), fileTypes, digestType);
 
            if (pos != -1) { // achou

                // verifica se o conteudo eh igual
                if (fileDigests.get(pos).contentEquals(calcHex(calcDigests.get(i)))) {
                    status = STATUS.OK; // conteudo igual
                } else {
                    status = STATUS.NOTOK; // conteudo diferente
                }

            } else {
                // nao achou!
                // insere no arquivo
                writeToEOF(digestListFiles.get(i) + " " + 
                        digestType + " " +
                        calcHex(calcDigests.get(i)) + "\n", digestListFile);
                
                fileNames.add( digestListFiles.get(i) );
                fileTypes.add( digestType );
                fileDigests.add( calcHex(calcDigests.get(i) ) );

                status = STATUS.NOTFOUND;
            }

            System.out.println(digestListFiles.get(i) 
                    + " " + digestType
                    + " " + calcHex(calcDigests.get(i))
                    + " (" + status.toString() + ")");
        }
    }
}