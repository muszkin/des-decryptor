package Des;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class DesEncrypter
{
    private static DesEncrypter desEncrypter = null;
    private Cipher ecipher;
    private Cipher dcipher;
    byte[] salt = { 73, -104, -56, 50, 86, 53, -29, 3 };
    int iterationCount = 19;
    public String localKey = "";
    public String localPBEKey = "";

    public DesEncrypter(String paramString,int iteration)
    {
        iterationCount = iteration;
        try
        {
            PBEKeySpec localPBEKeySpec = new PBEKeySpec(paramString.toCharArray(), this.salt, this.iterationCount);
            SecretKey localSecretKey = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(localPBEKeySpec);
            this.ecipher = Cipher.getInstance(localSecretKey.getAlgorithm());
            this.dcipher = Cipher.getInstance(localSecretKey.getAlgorithm());
            PBEParameterSpec localPBEParameterSpec = new PBEParameterSpec(this.salt, this.iterationCount);
            this.ecipher.init(1, localSecretKey, localPBEParameterSpec);
            this.dcipher.init(2, localSecretKey, localPBEParameterSpec);
            this.localKey = localSecretKey.toString();
            this.localPBEKey = localPBEParameterSpec.toString();
        }
        catch (Exception localException)
        {
            System.out.println("Error: DesEncrypter: " + localException.getMessage());
        }
    }

    public static DesEncrypter getInstatnce(String key,int iterations)
    {
        if (desEncrypter == null) {
            desEncrypter = new DesEncrypter(key,iterations);
        }
        return desEncrypter;
    }

    public byte[] encrypt(byte[] paramArrayOfByte)
    {
        if (paramArrayOfByte == null) {
            return null;
        }
        try
        {
            return this.ecipher.doFinal(paramArrayOfByte);
        }
        catch (Exception localException)
        {
            System.out.println("Error: DesEncrypter.encrypt: " + localException.getMessage());
        }
        return null;
    }

    public byte[] decrypt(byte[] paramArrayOfByte)
    {
        if (paramArrayOfByte == null) {
            return null;
        }
        try
        {
            return this.dcipher.doFinal(paramArrayOfByte);
        }
        catch (Exception localException)
        {
            System.out.println("Error: DesEncrypter.decrypt: " + localException.getMessage());
        }
        return null;
    }
}
