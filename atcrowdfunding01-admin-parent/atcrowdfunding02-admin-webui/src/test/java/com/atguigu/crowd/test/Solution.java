//package com.atguigu.crowd.test;
//
//public class Solution {
//    int a,b;
//    char [][] array ;
//    char [][] chars;
//    public  int minimalSteps(String[] maze) {
//        if(maze == null)
//            return 0;
//        chars = array = new char [maze[0].length()] [maze.length];
//        for(int i = 0;i<maze.length;i++){
//            array[i] = maze[i].toCharArray();
//        }
//        minimalStepsHandler(0, 0, array);
//
//        return  0;
//    }
//
//    public  int minimalStepsHandler(int m, int n, char[][] chars) {
//        if(m<0 || n <0 || n>chars.length || m>chars[0].length ||  chars[m][n]=='#')
//            return 0;
//        if(chars[m-1][n] == 'M')
//            minimalStepsHandler(m - 1, n, chars);
//        minimalStepsHandler(m + 1, n, chars);
//        minimalStepsHandler(m, n - 1, chars);
//        minimalStepsHandler(m, n + 1, chars);
//        return  0;
//    }
//
//    public  int minimalStepsHandlerFindTone(int m,int n,int x,int y,int [][] hasFind){
//        if (m == x && n == y) {
//            return 1;
//        }
//        if(m<0 || n <0 || n>chars.length || m>chars[0].length ||  chars[m][n]=='#')
//            return chars.length*chars[0].length;
//
//        if(m -1 >= 0 && hasFind[m-1][n] != 1) {
//            int[][] copy = new int[hasFind.length][hasFind[0].length];
//            for (int i = 0; i < hasFind.length; i++) {
//                for (int j = 0; j < hasFind[0].length; i++) {
//                    copy[i][j] = hasFind[i][j];
//                }
//            }
//            copy[m][n] = 1;
//            minimalStepsHandlerFindTone(m - 1, n, x, y, new int[][] q);
//        }
//        if(m + 1 < chars.length && hasFind[m-1][n] != 1) {
//            int[][] copy = new int[hasFind.length][hasFind[0].length];
//            for (int i = 0; i < hasFind.length; i++) {
//                for (int j = 0; j < hasFind[0].length; i++) {
//                    copy[i][j] = hasFind[i][j];
//                }
//            }
//            copy[m][n] = 1;
////            minimalStepsHandlerFindTone(m + 1, n, x, y, new int[][] q);
//        }
//        if(n -1 >= 0 && hasFind[m-1][n] != 1) {
//            int[][] copy = new int[hasFind.length][hasFind[0].length];
//            for (int i = 0; i < hasFind.length; i++) {
//                for (int j = 0; j < hasFind[0].length; i++) {
//                    copy[i][j] = hasFind[i][j];
//                }
//            }
//            copy[m][n] = 1;
//            minimalStepsHandlerFindTone(m , n - 1 , x, y, new int[][]);
//        }
//        if(n + 1 >= 0 && hasFind[m-1][n] != 1) {
//            int[][] copy = new int[hasFind.length][hasFind[0].length];
//            for (int i = 0; i < hasFind.length; i++) {
//                for (int j = 0; j < hasFind[0].length; i++) {
//                    copy[i][j] = hasFind[i][j];
//                }
//            }
//            copy[m][n] = 1;
//            minimalStepsHandlerFindTone(m - 1, n, x, y, new int[][]);
//        }
//
//        return 0;
//
//    }
//
//        /**
//         *  ["S#O", "M..", "M.T"]
//         * @param args
//         */
////    public static void main(String[] args) {
////        String s1 = "S#O";
////        String s2 = "M..";
////        String s3 = "M.T";
////        String[] strings = {s1, s2,s3};
////        Solution.minimalSteps(strings);
////    }
//}