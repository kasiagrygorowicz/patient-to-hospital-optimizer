package algorithm;

import data.elements.Hospital;

public class Dijkstra {

    public static Hospital[] calculateTheNearestHospitals(double[][] matrix, Hospital[] orderOfHospitals, Hospital from) {

        int amountOfVertices = matrix.length;
        int number = -1;

        for (int i = 0; i < orderOfHospitals.length; i++) {
            if (orderOfHospitals[i].getId() == from.getId()) {
                number = i;
            }
        }

        boolean[] tab = new boolean[amountOfVertices];

        double[] distance = new double[amountOfVertices];

        for (int i = 0; i < amountOfVertices; i++) {
            distance[i] = Double.MAX_VALUE;
            tab[i] = false;
        }

        distance[number] = 0;

        for (int i = 0; i < amountOfVertices - 1; i++) {

            double min = Double.MAX_VALUE;
            int index = -1;
            for (int j = 0; j < amountOfVertices; j++) {
                if (!tab[j] && distance[j] <= min) {
                    min = distance[j];
                    index = j;
                }
            }
            tab[index] = true;
            for (int j = 0; j < amountOfVertices; j++) {
                if (matrix[index][j] != 0 && !tab[j] && distance[index] != Double.MAX_VALUE && distance[index] + matrix[index][j] < distance[j]) {
                    distance[j] = distance[index] + matrix[index][j];
                }
            }
        }

        for (int i = 0; i < orderOfHospitals.length; i++) {
            double tmp = distance[i];
            Hospital tmp1 = orderOfHospitals[i];
            int j = i - 1;
            for (; j >= 0 && distance[j] > tmp; j--) {
                distance[j + 1] = distance[j];
                orderOfHospitals[j + 1] = orderOfHospitals[j];
            }
            distance[j + 1] = tmp;
            orderOfHospitals[j + 1] = tmp1;
        }

        Hospital[] result = new Hospital[orderOfHospitals.length - 1];
        System.arraycopy(orderOfHospitals, 1, result, 0, result.length);

        return result;
    }
}
