package com.test;

public class Test {

    public static void main(String[] arge) {
        //买入价钱计算();
        盈利收益计算("2018-07-10",22f);
        // 买入价钱计算();
    }

    public static void 盈利收益计算(String date, float 股价) {

        float sb = 3200 * 股价;

        float a1 = 1000;
        float b1 = 1000;
        float c1 = 600;
        float d1 = 600;

        float a2 = 16741;
        float b2 = 16741;
        float c2 = 10045;
        float d2 = 10045;

        float a3 = a1 * 股价;
        float b3 = b1 * 股价;
        float c3 = c1 * 股价;
        float d3 = d1 * 股价;
        //16741-11100   27659
        System.out.println(date + " 盈亏明细");
        System.out.println("收市价钱:" + 股价 + " 市场价:" + sb);
        System.out.println("   原始价  市场价  收益");
        System.out.println("鸿 " + d2 + " " + d3 + " " + (d3 - d2));
        System.out.println("B  " + c2 + " " + c3 + " " + (c3 - c2));
        System.out.println("孙 " + b2 + " " + b3 + " " + (b3 - b2));
        System.out.println("思 " + a2 + " " + a3 + " " + (a3 - a2));
        System.out.println("总 " + (a2 + b2 + c2 + d2) + " " + (a3 + b3 + c3 + d3) + " " + ((a3 + b3 + c3 + d3) - (a2 + b2 + c2 + d2)));

    }

    public static void 买入价钱计算() {
        float a = 22200;
        float b = 22200;
        float c = 11100;
        float d = 11100;
        int fs = 32;
        float a1 = 10;
        float b1 = 10;
        float c1 = 6;
        float d1 = 6;

        float sum = 66600;
        float remaining = 13029;

        float total_purchases = sum - remaining;

        float a3 = Math.round(total_purchases * (a1 / fs));
        float b3 = Math.round(total_purchases * (b1 / fs));
        float c3 = Math.round(total_purchases * (c1 / fs));
        float d3 = Math.round(total_purchases * (d1 / fs));

        float a4 = a - a3;
        float b4 = b - b3;
        float c4 = c - c3;
        float d4 = d - d3;

        System.out.println("投资明细");
        System.out.println("  投资额    购买额(股数) 剩余额度                   ");
        System.out.println("鸿 " + (int) c + "    " + (int) c3 + "(" + 100 * (int) c1 + " )" + "    " + (int) c4);
        System.out.println("B  " + (int) d + "    " + (int) d3 + "(" + 100 * (int) d1 + " )" + "    " + (int) d4);
        System.out.println("孙 " + (int) b + "    " + (int) b3 + "(" + 100 * (int) b1 + ")" + "    " + (int) b4);
        System.out.println("思 " + (int) a + "    " + (int) a3 + "(" + 100 * (int) a1 + ")" + "    " + (int) a4);
        System.out.println("总 " + (int) (a + b + c + d) + "    " + (int) (a3 + b3 + c3 + d3) + "(" + 100 * (int) (a1 + b1 + c1 + d1) + ")" + "    " + (int) (a4 + b4 + c4 + d4));
        System.out.println("买入股价16.74 存钱总数:" + sum + " 账户剩余:" + remaining);
    }

}
