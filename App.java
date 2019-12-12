package App;

import java.util.stream.IntStream;

public class App {

    public static int[][] localRec(int[][] A, int[][] B, int N) {
        int n = N;
        int m = powOf2(n);
        int[][] APrep = new int[m][m];
        int[][] BPrep = new int[m][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                APrep[i][j] = A[i][j];
                BPrep[i][j] = B[i][j];
            }
        }

        int[][] CPrep = reсursive(APrep, BPrep);
        int[][] C = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = CPrep[i][j];
            }
        }
        return C;
    }

    private static int[][] reсursive(int[][] A, int[][] B) {
        int n = A.length;

        if (n <= 1) {
            int[][] C = new int[n][n];
            IntStream.range(0, n).forEach(i -> IntStream.range(0, n).forEach(k -> IntStream.range(0, n).forEach(j -> C[i][j] += A[i][k] * B[k][j])));
            return C;
        }

        // initializing the new sub-matrices
        int newSize = n / 2;
        int[][] a11 = new int[newSize][newSize];
        int[][] a12 = new int[newSize][newSize];
        int[][] a21 = new int[newSize][newSize];
        int[][] a22 = new int[newSize][newSize];

        int[][] b11 = new int[newSize][newSize];
        int[][] b12 = new int[newSize][newSize];
        int[][] b21 = new int[newSize][newSize];
        int[][] b22 = new int[newSize][newSize];

        int[][] aResult = new int[newSize][newSize];
        int[][] bResult = new int[newSize][newSize];

        // dividing the matrices in 4 sub-matrices:
        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < newSize; j++) {
                a11[i][j] = A[i][j]; // top left
                a12[i][j] = A[i][j + newSize]; // top right
                a21[i][j] = A[i + newSize][j]; // bottom left
                a22[i][j] = A[i + newSize][j + newSize]; // bottom right

                b11[i][j] = B[i][j]; // top left
                b12[i][j] = B[i][j + newSize]; // top right
                b21[i][j] = B[i + newSize][j]; // bottom left
                b22[i][j] = B[i + newSize][j + newSize]; // bottom right
            }
        }

        // calculating p1 to p7:
        aResult = sum(a11, a22);
        bResult = sum(b11, b22);
        int[][] p1 = reсursive(aResult, bResult);
        // p1 = (a11+a22) * (b11+b22)

        aResult = sum(a21, a22); // a21 + a22
        int[][] p2 = reсursive(aResult, b11); // p2 = (a21+a22) * (b11)

        bResult = sub(b12, b22); // b12 - b22
        int[][] p3 = reсursive(a11, bResult);
        // p3 = (a11) * (b12 - b22)

        bResult = sub(b21, b11); // b21 - b11
        int[][] p4 = reсursive(a22, bResult);
        // p4 = (a22) * (b21 - b11)

        aResult = sum(a11, a12); // a11 + a12
        int[][] p5 = reсursive(aResult, b22);
        // p5 = (a11+a12) * (b22)

        aResult = sub(a21, a11); // a21 - a11
        bResult = sum(b11, b12); // b11 + b12
        int[][] p6 = reсursive(aResult, bResult);
        // p6 = (a21-a11) * (b11+b12)

        aResult = sub(a12, a22); // a12 - a22
        bResult = sum(b21, b22); // b21 + b22
        int[][] p7 = reсursive(aResult, bResult);
        // p7 = (a12-a22) * (b21+b22)

        // calculating c21, c21, c11 e c22:
        int[][] c12 = sum(p3, p5); // c12 = p3 + p5
        int[][] c21 = sum(p2, p4); // c21 = p2 + p4

        aResult = sum(p1, p4); // p1 + p4
        bResult = sum(aResult, p7); // p1 + p4 + p7
        int[][] c11 = sub(bResult, p5);
        // c11 = p1 + p4 - p5 + p7

        aResult = sum(p1, p3); // p1 + p3
        bResult = sum(aResult, p6); // p1 + p3 + p6
        int[][] c22 = sub(bResult, p2);
        // c22 = p1 + p3 - p2 + p6

        // grouping the results obtained in a single matrix:
        int[][] C = new int[n][n];
        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < newSize; j++) {
                C[i][j] = c11[i][j];
                C[i][j + newSize] = c12[i][j];
                C[i + newSize][j] = c21[i][j];
                C[i + newSize][j + newSize] = c22[i][j];
            }
        }
        return C;
    }

    public static void main(String[] args) {
        int n = 7;

        if (args.length > 0) {
            n = Integer.parseInt(args[0]);
        }

        int[][] A, B, C, C0;
        A = new int[n][n];
        B = new int[n][n];

        C0 = new int[n][n];
        int i, j, k;
        System.out.println("Matrix A:");
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) {
                if (i == j) {
                    A[i][j] =  (i + 1) * (i + 2);
                } else{
                    A[i][j] =  0;
                }
                System.out.print(A[i][j] + (j == n - 1 ? "\n" : "\t"));
            }
        }

        System.out.println("Matrix B:");
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) {
                int r = (int) ((Math.random() + 1) * n);
                if (i + j < n)
                    B[i][j] = r;  // above the diagonal
                else {
                    B[i][j] = 0;
                }
                System.out.print(B[i][j]  + (j == n - 1 ? "\n" : "\t"));
            }
        }

        System.out.println("Validation results:");
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) {
                for (k = 0; k < n; k++) {
                    C0[i][j] += A[i][k] * B[k][j];
                }
                System.out.print(C0[i][j] + (j == n - 1 ? "\n" : "\t"));
            }
        }

        System.out.println("Multiplication A x B:");
        C = localRec(A, B, n);
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) {
                System.out.print(C[i][j] + (j == n - 1 ? "\n" : "\t"));
            }
        }
    }

    private static int[][] sum(int[][] m1, int[][] m2) {
        int n = m1.length;
        int[][] m = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                m[i][j] = m1[i][j] + m2[i][j];
            }
        }
        return m;
    }

    private static int[][] sub(int[][] m1, int[][] m2) {
        int n = m1.length;
        int[][] m = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                m[i][j] = m1[i][j] - m2[i][j];
            }
        }
        return m;
    }

    private static int powOf2(int n) {
        int log2 = (int) Math.ceil(Math.log(n) / Math.log(2));
        return (int) Math.pow(2, log2);
    }
}

