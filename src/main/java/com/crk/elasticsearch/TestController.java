package com.crk.elasticsearch;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.Unsafe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("test")
public class TestController {
    @RequestMapping("aa")
    public String aa() {
        return "aa";
    }

    public static void main(String[] args) {
//       Unsafe unsafe =  Unsafe.getUnsafe();
//
//        List<Integer> list = Arrays.asList(100, 200, 300);
//        for (Integer o : list) {
//            double price = 0.15*o;
//            System.out.println(price);
//        }


//       double result = list.stream().map((cost)->0.15*cost).reduce((sum,cost)->sum+cost).get();
//       System.out.println(result);

//        IntSummaryStatistics statistics = list.stream().mapToInt((cost)->cost).summaryStatistics();
//        System.out.println(statistics.getAverage());
//        System.out.println(statistics.getMax());
//        String[] strs = new String[]{"a", "a", "b", "b", "b", "c", "c", "d"};
        TestController testController = new TestController();
//        String[] BB = testController.getAAA(strs);
//        for (int i = 0; i < BB.length; i++) {
//            String s = BB[i];
//            System.out.println(s);
//        }

        int[] integers = new int[]{3,3,5,2,1,3,1,4};
        int[] test2 = new int[]{9,4,8,6,7,8,6,5,10};
        int aa = testController.maxProfit(integers,1);
        System.out.println(aa);
        int bb = testController.maxProfit1(integers,1);
        System.out.println(bb);
//        testController.getSoulation(test2);
//        testController.test();
    }


    public String[] getAAA(String[] strs) {
        ArrayList<String> list = new ArrayList<String>();
        String temp = null;
        int count = 0;
        for (int i = 0; i < strs.length; i++) {
            if (i != 0) {
                if (temp.equals(strs[i])) {
                    count++;
                    if (i == strs.length - 1) {
                        list.add(temp);
                        list.add(count + "");
                    }
                } else {
                    list.add(temp);
                    list.add(count + "");
                    temp = strs[i];
                    count = 1;
                    if (i == strs.length - 1) {
                        list.add(temp);
                        list.add(count + "");
                    }
                }
            } else {
                temp = strs[i];
                count = 1;
            }
        }
        String[] result = new String[list.size()];
        return list.toArray(result);
    }

    public int maxProfit(int[] prices,int k) {
        /**
         对于任意一天考虑四个变量:
         fstBuy: 在该天第一次买入股票可获得的最大收益
         fstSell: 在该天第一次卖出股票可获得的最大收益
         secBuy: 在该天第二次买入股票可获得的最大收益
         secSell: 在该天第二次卖出股票可获得的最大收益
         分别对四个变量进行相应的更新, 最后secSell就是最大
         收益值(secSell >= fstSell)
         **/
        int fstBuy = Integer.MIN_VALUE, fstSell = 0;
        int secBuy = Integer.MIN_VALUE, secSell = 0;

        for(int p : prices) {

                    fstBuy = Math.max(fstBuy,-p);
                    fstSell = Math.max(fstSell, fstBuy + p);
                    secBuy = Math.max(secBuy, fstSell - p);
                    secSell = Math.max(secSell, secBuy + p);

        }
        return secSell;
    }

    public int maxProfit1(int[] prices,int k) {
        int len = prices.length;
        if (len == 0)
            return 0;
        int[] pre = new int[len];
        int[] post = new int[len];
        int minPrice = prices[0];
        for (int i = 1; i<len; i++){                //计算第i点之前的最大利润pre
            minPrice = Math.min(minPrice, prices[i]);
            pre[i] = Math.max(pre[i - 1], prices[i] - minPrice);
        }
        int maxPrice = prices[len - 1];
        for (int i = len - 2; i >= 0; i--){        //计算第i点之后的最大利润post
            maxPrice = Math.max(maxPrice, prices[i]);
            post[i] = Math.max(post[i + 1], maxPrice - prices[i]);
        }
        int maxProfit = 0;
        for (int i = 0; i<len; i++){            //计算第i点的，pre[i]与post[i]之和的最大值，赋值给maxProfit
            maxProfit = Math.max(maxProfit, pre[i] + post[i]);
        }
        return maxProfit;
    }



    public int[] sellMax(int aPrice,int bPrice,int currentPrice,int price){
        int[] result = new int[2];
        if (aPrice>=bPrice){
            result[0] = aPrice;
            result[1] = currentPrice;
        }else {
            result[0] = bPrice;
            result[1] = currentPrice+price;
        }
        return result;
    }

    //nums为给定的一组数字
    public void getSoulation(int[] nums){
        Arrays.sort(nums);
        int[][] result = new int[3][3];
        int flag = checkAndTry(result,nums);
        if(flag==1){
            for (int[] ints : result) {
                for (int anInt : ints) {
                    System.out.print(anInt+"   ");
                }
                System.out.println("");
            }
        }else {
            System.out.println("无解");
        }

    }

    public int checkAndTry(int[][] b,int[] a)

    {

        int x = 0, y = 1;

        b[x][y] = a[0]; //将最小的数字赋值给b[0][1]，九宫格中第一行第二列的位置

        for (int k = 1; k < 9; k++)

        {

            int xNew = x - 1;

            int yNew = y + 1;   //依次向右向上寻找，将下一个数字放在该位置

            if (xNew < 0)

                xNew = 2;

            if (yNew > 2)

                yNew = 0;

            if (b[xNew][yNew] != 0){    //若该位置有数字了，则向下寻找，将下一个数字放在该位置

                xNew = x + 1;

                yNew = y;

            }

            b[xNew][yNew] = a[k];

            x = xNew;

            y = yNew;

        }

        int row1 = b[0][0] + b[0][1] + b[0][2];     //计算九宫格中每一行，列，斜对角线上的值

        int row2 = b[1][0] + b[1][1] + b[1][2];

        int row3 = b[2][0] + b[2][1] + b[2][2];

        int col1 = b[0][0] + b[1][0] + b[2][0];

        int col2 = b[0][1] + b[1][1] + b[2][1];

        int col3 = b[0][2] + b[1][2] + b[2][2];

        int dig1 = b[0][0] + b[1][1] + b[2][2];

        int dig2 = b[2][0] + b[1][1] + b[0][2];

        int flag = 0;   //比较横竖斜方向上的的值是否相等

        if (row1 == row2&&row1 == row3&&row1 == col1&&row1 == col2&&row1 == col3&&row1 == dig1&&row1 == dig2)

            flag = 1;

        return flag;

    }

    int test()
    {
        int br, i, j, num = 0;
        int[][] row = new int[100][2];
        int[][] col =new int[100][2];
        int k = 2, area1 = 0, area2 = 0; //第一大岛的值全部为2
        int[][] a = { { 1, 1, 1, 1, 1, 1 },
        { 1, 1, 0, 0, 0, 1 },
        { 1, 0, 0, 0, 1, 0 },
        { 1, 1, 0, 1, 1, 1 },
        { 0, 1, 0, 1, 0, 0 },
        { 1, 1, 1, 1, 1, 1 } };
        Arrays.fill(row,-1);
        Arrays.fill(col,-1);
        br = 6;
        for (i = 0; i<br; i++)    //判断每行中最左边的1和最右边的1的标号row[i][0],row[i][1],每列中最上边的1和最下边的1的标号col[i][0],col[i][1]
        {
            for (j = 0; j<br; j++)
            {
                if (a[i][j] == 1)
                {
                    if (row[i][0] == -1)
                        row[i][0] = j;
                    if (col[j][0] == -1)
                        col[j][0] = i;
                    row[i][1] = j;
                    col[j][1] = i;
                }
            }
        }
        for (i = 0; i<br; i++)
        {
            for (j = 0; j<br; j++)
            {
                if (a[i][j] == 0)
                {
                    if (j>row[i][0] && j<row[i][1] && i>col[j][0] && i<col[j][1]) //如果该点左边，右边，上边，下边有1，则判断该点为岛
                    {
                        tuse(a,0,0, i, j, k);        //进入该点，递归，将该点的连通区域都标记为k
                        k++;
                    }
                }
            }
        }

//        computeArea(a,0,0, br, area1, area2);
        if (area2 != 0)                //如果有第二大小岛，则输出area2的面积
            System.out.println(area2);
        else
            System.out.println(area1);
        return 0;
    }
//
    int N = 100;

    void tuse(int[][] a,int start,int end,int i,int j,int k){//将数组a[0][0]的地址传给指针p，其位置为i,j，连通区域的标号k//判断为小岛的点赋值为2，并进入该点上下左右的点，递归的进行扩展，将连通在一起的点，都赋值为k
        a[i*N][j] = k;
        if(a[start+i*N][end+j-1]==0){//左边
            tuse(a,start+i*N,end+j-1 ,i, j - 1, k);
        }
        if (a[start+(i - 1)*N][end+j]==1){//上边
            tuse(a,start+(i - 1)*N,end+j ,i-1, j, k);
        }
        if (a[start+i*N][end+j+1]==1){//右边
            tuse(a,start+i*N,end+j+1 ,i, j + 1, k);
        }
        if (a[start+(i+1)*N][end+j]==1){//右边
            tuse(a,start+(i+1)*N,end+j ,i+1, j , k);
        }
    }

//
//    void computeArea(int[][] b,int p0,int p1, int br, int p1, int p2)    //计算第一大区域的面积与第二大区域的面积，返回给指针p1,p2
//    {
//        for (int i = 0; i < br; i++)
//            for (int j = 0; j < br; j++)
//            {
//                if (b[p0+i*N][j] == 2)
//                p1++;
//                if (b[p0+i*N][j] == 3)
//                p2++;
//            }
//    }

//    int maxProfit(vector<int> &prices) {
//        int len = prices.size();
//        if (len == 0)
//            return 0;
//        vector<int> pre(len);
//        vector<int> post(len);
//        int minPrice = prices[0];
//        for (int i = 1; i<len; i++){                //计算第i点之前的最大利润pre
//            minPrice = min(minPrice, prices[i]);
//            pre[i] = max(pre[i - 1], prices[i] - minPrice);
//        }
//        int maxPrice = prices[len - 1];
//        for (int i = len - 2; i >= 0; i--){        //计算第i点之后的最大利润post
//            maxPrice = max(maxPrice, prices[i]);
//            post[i] = max(post[i + 1], maxPrice - prices[i]);
//        }
//        int maxProfit = 0;
//        for (int i = 0; i<len; i++){            //计算第i点的，pre[i]与post[i]之和的最大值，赋值给maxProfit
//            maxProfit = max(maxProfit, pre[i] + post[i]);
//        }
//        return maxProfit;
//    }


}
