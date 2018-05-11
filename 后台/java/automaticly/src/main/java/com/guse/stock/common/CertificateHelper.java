package com.guse.stock.common;

import java.util.Random;

/*
 * 凭证加密解密算法
 * Author : Joey
 * Date: 2017-07-02
 */
public class CertificateHelper {


	    private int start = 32;
	    private int length = 5;
	    
	    /**
	     * 凭证加密
	     */
	    public String encrypt(String cer){
	    	cer = this.ins(cer,this.ordsum(cer));        
	        return cer;
	    }

	    /**
	     * 凭证解密
	     */
	    public String decrypt(String cer) {
	    	String str = this.del(cer, this.ordsum(cer));
	    	return str;
	    }
	    
	    /**
	    *根据ascii码计算地址
	    **/
	    private int ordsum(String cer){
	    	int position = 0;
	    	String str = cer.substring(this.start, this.start+this.length);
	    	char [] chars = str.toCharArray();
	    	for(int i = 0 ; i < chars.length ; i ++) {
	    		position += Integer.valueOf(chars[i]);
	    	}
	    	return position;
	    }
	    
	    
	    /**
	     * 在指定位置插入随机字符
	     */
	    private String ins(String cer, int position){
	        Random rand = new Random();
	    	int randNum = rand.nextInt(122 - 97 + 1) + 97;
	    	char ch = (char)randNum;
	        return cer.substring(0, position) + ch + cer.substring(position,cer.length());  
	    }
	    
	    
	    /**
	     * 删除指定位置的随机字符
	     */
	    private String  del(String cer, int position) {
	        return cer.substring(0,position) + cer.substring(position+1);
	    }
	    
	    
	    public static void main( String[] args ) {
	    	CertificateHelper ch = new CertificateHelper();
	    	String str = "ewoJInNpZ25hdHVyZSIgPSAiQTRaT3BQamU0N2JBeW9vcVNBTEdlem5IeXFRMmd0RnRMZHU2WUtISWc0Q3FGbHBvTlpYRlVNalJTT283M1pJM0hTYklucllKZmoxSE1vVGcwTEhTZHQrc0c2QnRJdS92SGxuMFVYQUpUWWlaK2pjQ0llU3grMGsyeXIxR0tHTlJTMk5MRTM2TDhCTmY2RFY1OS95bTREb0t5U1oyRXZUYmlqeTRNS2ZEcTkzK2treTkvaG93b1V1TTBUaGljZUpNV1l2Z3ovczVWd2ZPTHZMM2hVTjViaER6OFFnUUx4RkQyeUJEWCtsVGZ5aXQxZFZudUtKVjVLbEtNa1NxTGtYYnJTcXZWZ1R5R2VhaWxDaGVVMEZTbEsxUzRmVzd4Wi81U1VnalBXZkFNyUnNwL081djZxYWwyYWM5UlBhclRJWHozdXc5RHp6VWw0azZkRkhjU051N0g2VUFBQVdBTUlJRmZEQ0NCR1NnQXdJQkFnSUlEdXRYaCtlZUNZMHdEUVlKS29aSWh2Y05BUUVGQlFBd2daWXhDekFKQmdOVkJBWVRBbFZUTVJNd0VRWURWUVFLREFwQmNIQnNaU0JKYm1NdU1Td3dLZ1lEVlFRTERDTkJjSEJzWlNCWGIzSnNaSGRwWkdVZ1JHVjJaV3h2Y0dWeUlGSmxiR0YwYVc5dWN6RkVNRUlHQTFVRUF3dzdRWEJ3YkdVZ1YyOXliR1IzYVdSbElFUmxkbVZzYjNCbGNpQlNaV3hoZEdsdmJuTWdRMlZ5ZEdsbWFXTmhkR2x2YmlCQmRYUm9iM0pwZEhrd0hoY05NVFV4TVRFek1ESXhOVEE1V2hjTk1qTXdNakEzTWpFME9EUTNXakNCaVRFM01EVUdBMVVFQXd3dVRXRmpJRUZ3Y0NCVGRHOXlaU0JoYm1RZ2FWUjFibVZ6SUZOMGIzSmxJRkpsWTJWcGNIUWdVMmxuYm1sdVp6RXNNQ29HQTFVRUN3d2pRWEJ3YkdVZ1YyOXliR1IzYVdSbElFUmxkbVZzYjNCbGNpQlNaV3hoZEdsdmJuTXhFekFSQmdOVkJBb01Da0Z3Y0d4bElFbHVZeTR4Q3pBSkJnTlZCQVlUQWxWVE1JSUJJakFOQmdrcWhraUc5dzBCQVFFRkFBT0NBUThBTUlJQkNnS0NBUUVBcGMrQi9TV2lnVnZXaCswajJqTWNqdUlqd0tYRUpzczl4cC9zU2cxVmh2K2tBdGVYeWpsVWJYMS9zbFFZbmNRc1VuR09aSHVDem9tNlNkWUk1YlNJY2M4L1cwWXV4c1FkdUFPcFdLSUVQaUY0MWR1MzBJNFNqWU5NV3lwb041UEM4cjBleE5LaERFcFlVcXNTNCszZEg1Z1ZrRFV0d3N3U3lvMUlnZmRZZUZScjZJd3hOaDlLQmd4SFZQTTNrTGl5a29sOVg2U0ZTdUhBbk9DNnBMdUNsMlAwSzVQQi9UNXZ5c0gxUEttUFVockFKUXAyRHQ3K21mNy93bXYxVzE2c2MxRkpDRmFKekVPUXpJNkJBdENnbDdaY3NhRnBhWWVRRUdnbUpqbTRIUkJ6c0FwZHhYUFEzM1k3MkMzWmlCN2o3QWZQNG83UTAvb21WWUh2NGdOSkl3SURBUUFCbzRJQjF6Q0NBZE13UHdZSUt3WUJCUVVIQVFFRU16QXhNQzhHQ0NzR0FRVUZCekFCaGlOb2RIUndPaTh2YjJOemNDNWhjSEJzWlM1amIyMHZiMk56Y0RBekxYZDNaSEl3TkRBZEJnTlZIUTRFRmdRVWthU2MvTVIydDUrZ2l2Uk45WTgyWGUwckJJVXdEQVlEVlIwVEFRSC9CQUl3QURBZkJnTlZIU01FR0RBV2dCU0lKeGNKcWJZWVlJdnM2N3IyUjFuRlVsU2p0ekNDQVI0R0ExVWRJQVNDQVJVd2dnRVJNSUlCRFFZS0tvWklodmRqWkFVR0FUQ0IvakNCd3dZSUt3WUJCUVVIQWdJd2diWU1nYk5TWld4cFlXNWpaU0J2YmlCMGFHbHpJR05sY25ScFptbGpZWFJsSUdKNUlHRnVlU0J3WVhKMGVTQmhjM04xYldWeklHRmpZMlZ3ZEdGdVkyVWdiMllnZEdobElIUm9aVzRnWVhCd2JHbGpZV0pzWlNCemRHRnVaR0Z5WkNCMFpYSnRjeUJoYm1RZ1kyOXVaR2wwYVc5dWN5QnZaaUIxYzJVc0lHTmxjblJwWm1sallYUmxJSEJ2YkdsamVTQmhibVFnWTJWeWRHbG1hV05oZEdsdmJpQndjbUZqZEdsalpTQnpkR0YwWlcxbGJuUnpMakEyQmdnckJnRUZCUWNDQVJZcWFIUjBjRG92TDNkM2R5NWhjSEJzWlM1amIyMHZZMlZ5ZEdsbWFXTmhkR1ZoZFhSb2IzSnBkSGt2TUE0R0ExVWREd0VCL3dRRUF3SUhnREFRQmdvcWhraUc5Mk5rQmdzQkJBSUZBREFOQmdrcWhraUc5dzBCQVFVRkFBT0NBUUVBRGFZYjB5NDk0MXNyQjI1Q2xtelQ2SXhETUlKZjRGelJqYjY5RDcwYS9DV1MyNHlGdzRCWjMrUGkxeTRGRkt3TjI3YTQvdncxTG56THJSZHJqbjhmNUhlNXNXZVZ0Qk5lcGhtR2R2aGFJSlhuWTR3UGMvem83Y1lmcnBuNFpVaGNvT0FvT3NBUU55MjVvQVE1SDNPNXlBWDk4dDUvR2lvcWJpc0IvS0FnWE5ucmZTZW1NL2oxbU9DK1JOdXhUR2Y4YmdwUHllSUdxTktYODZlT2ExR2lXb1IxWmRFV0JHTGp3Vi8xQ0tuUGFObVNBTW5CakxQNGpRQmt1bGhnd0h5dmozWEthYmxiS3RZZGFHNllRdlZNcHpjWm04dzdISG9aUS9PamJiOUlZQVlNTnBJcjdONFl0UkhhTFNQUWp2eWdhWndYRzU2QWV6bEhSVEJoTDhjVHFBPT0iOwoJInB1cmNoYXNlLWluZm8iID0gImV3b0pJbTl5YVdkcGJtRnNMWEIxY21Ob1lYTmxMV1JoZEdVdGNITjBJaUE5SUNJeU1ERTNMVEE0TFRFd0lEQTJPak0xT2pJMklFRnRaWEpwWTJFdlRHOXpYMEZ1WjJWc1pYTWlPd29KSW5CMWNtTm9ZWE5sTFdSaGRHVXRiWE1pSUQwZ0lqRTFNREl6TnpJeE1qWTJORFlpT3dvSkluVnVhWEYxWlMxcFpHVnVkR2xtYVdWeUlpQTlJQ0l5WXpjM05EYzRaR0U1WkRBMU9USmhZamcwT0RRek9USmpOakEzTmpJMU0yRXhZVE0zTkRaa0lqc0tDU0p2Y21sbmFXNWhiQzEwY21GdWMyRmpkR2x2YmkxcFpDSWdQU0FpTXpVd01EQXdNalExTWpVd05USTFJanNLQ1NKaWRuSnpJaUE5SUNJd01EQTNJanNLQ1NKaGNIQXRhWFJsYlMxcFpDSWdQU0FpT1RnNU5qY3pPVFkwSWpzS0NTSjBjbUZ1YzJGamRHbHZiaTFwWkNJZ1BTQWlNelV3TURBd01qUTFNalV3TlRJMUlqc0tDU0p4ZFdGdWRHbDBlU0lnUFNBaU1TSTdDZ2tpYjNKcFoybHVZV3d0Y0hWeVkyaGhjMlV0WkdGMFpTMXRjeUlnUFNBaU1UVXdNak0zTWpFeU5qWTBOaUk3Q2draWRXNXBjWFZsTFhabGJtUnZjaTFwWkdWdWRHbG1hV1Z5SWlBOUlDSkVNREkzTnpNNVF5MDNOemxGTFRRMVFUWXRPVVZDUWkxQ1F6Y3pORU13TVRsRE5rWWlPd29KSW1sMFpXMHRhV1FpSUQwZ0lqRXdNemszT0Rrd05EWWlPd29KSW5abGNuTnBiMjR0WlhoMFpYSnVZV3d0YVdSbGJuUnBabWxsY2lJZ1BTQWlPREl5T1RZek16azJJanNLQ1NKd2NtOWtkV04wTFdsa0lpQTlJQ0pqYjIwdWRHVnVZMlZ1ZEM1emJXOWlZVEV3TVRCZk1UQWlPd29KSW5CMWNtTm9ZWE5sTFdSaGRHVWlJRDBnSWpJd01UY3RNRGd0TVRBZ01UTTZNelU2TWpZZ1JYUmpMMGROVkNJN0Nna2liM0pwWjJsdVlXd3RjSFZ5WTJoaGMyVXRaR0YwWlNJZ1BTQWlNakF4Tnkwd09DMHhNQ0F4TXpvek5Ub3lOaUJGZEdNdlIwMVVJanNLQ1NKaWFXUWlJRDBnSW1OdmJTNTBaVzVqWlc1MExuTnRiMkpoSWpzS0NTSndkWEpqYUdGelpTMWtZWFJsTFhCemRDSWdQU0FpTWpBeE55MHdPQzB4TUNBd05qb3pOVG95TmlCQmJXVnlhV05oTDB4dmMxOUJibWRsYkdWeklqc0tmUT09IjsKCSJwb2QiID0gIjM1IjsKCSJzaWduaW5nLXN0YXR1cyIgPSAiMCI7Cn0=";
//	    	System.out.println(str);
//	    	String en = ch.encrypt(str);
//	    	System.out.println(en);
	    	String dn = ch.decrypt(str);
	    	System.out.println(dn);
		}

}
