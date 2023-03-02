import java.util.*;
public class Prgrm9 {
    public static void main(String []args)
    {
        int dataset[][]={{1,1},{2,1},{4,3},{5,4},};
        int i,k=2;
        int c1[][]= new int[4][2];
        int c2[][]= new int[4][2];
        float m1[][]=new float[1][2];
        float m2[][]=new float[1][2];
        float temp1[][]=new float[1][2];
        float temp2[][]=new float[1][2];
        int s11=0,s12=0,s21=0,s22=0;
        double d1,d2;
        int i1=0,i2=0,itr=0;
        System.out.println("Dataset");
        for(i=0;i<4;i++){
            System.out.println(dataset[i][0]+" "+dataset[i][1]);
        }
        System.out.println("\n Number of cluSters:  "+k);
        m1[0][0]=1;
        m1[0][1]=1;
        m2[0][0]=2;
        m2[0][1]=2;
        while(!Arrays.deepEquals(m1,temp1) ||!Arrays.deepEquals(m2,temp2)){
        for(i=0;i<4;i++)
        {
            c1[i][0]=0;
            c1[i][1]=0;
            c2[i][0]=0;
            c2[i][1]=0;
        }
        i1=0;i2=0;
        for(i=0;i<4;i++){
            d1=Math.sqrt(Math.pow(dataset[i][0]-m1[0][0],2)+Math.pow(dataset[i][1]-m1[0][1],2));
            d2=Math.sqrt(Math.pow(dataset[i][0]-m2[0][0],2)+Math.pow(dataset[i][1]-m2[0][1],2));
            
            if(d1<d2)//actual condition
            {
                c1[i1][0]=dataset[i][0];
                c1[i1][1]=dataset[i][1];
                i1++;
            }
            else{
               c2[i2][0]=dataset[i][0];
                c2[i2][1]=dataset[i][1];
                i2++; 
            }
        //System.out.print( d1+"\t");
        //System.out.print( d2+"\n");
         
        }
         temp1[0][0]=m1[0][0]; 
         temp1[0][1]=m1[0][1];
         temp2[0][0]=m2[0][0];
         temp2[0][1]=m2[0][1];
         s11=0;s12=0;s21=0;s22=0;
         for(i=0;i<i1;i++)
         {
             s11+=c1[i][0];
             s12+=c1[i][1];
         }
         for(i=0;i<i2;i++)
         {
             s21+=c2[i][0];
             s22+=c2[i][1];
         }
         m1[0][0]=(float)s11/i1;
         m1[0][1]=(float)s12/i1;
         m2[0][0]=(float)s21/i2;
         m2[0][1]=(float)s22/i2;
         itr++;
        }
        System.out.println("final clusters:");
        System.out.println(" cluster1:");
        for(i=0;i<i1;i++){
            System.out.println(c1[i][0]+"  "+c1[i][1]);
        }
        System.out.println(" cluster2:");
        for(i=0;i<i2;i++){
            System.out.println(c2[i][0]+"  "+c2[i][1]);
        }
        System.out.println("\n final mean:");
        System.out.println("mean1:"+m1[0][0]+"   "+m1[0][1]);
        System.out.println("mean2:"+m2[0][0]+"   "+m2[0][1]);
        System.out.println("\n total iteration:"+itr);
    }
}
