package edu.stonybrook.cse308.gerrybackend.algorithms.measures;

import edu.stonybrook.cse308.gerrybackend.enums.measures.Compactness;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;

public interface CompactnessMeasure {

    class GraphTheoretical {
        static double computeCompactnessScore(DistrictNode district) {
            // TODO: fill in
            // reference https://www.researchgate.net/publication/311557290_Beyond_the_Circle_Measuring_District_Compactness_using_Graph_Theory
            return -1.0;
        }
    }

    class PolsbyPopper {
        static double computeCompactnessScore(DistrictNode district) {
            double area = district.getGeometry().getArea();
            double perimeter = district.getGeometry().getLength();
            return (4 * Math.PI * (area / (Math.pow(perimeter, 2))));
        }
    }

    class Schwartzberg {
        private static double computeCircumferenceOfSameAreaCircle(DistrictNode district) {
            double radius = Math.sqrt(district.getGeometry().getArea() / Math.PI);
            return 2 * Math.PI * radius;
        }

        static double computeCompactnessScore(DistrictNode district) {
            double perimeter = district.getGeometry().getLength();
            double circumference = Schwartzberg.computeCircumferenceOfSameAreaCircle(district);
            return 1 / (perimeter / circumference);
        }
    }

    class Reock {
        static double computeCompactnessScore(DistrictNode district) {
            Geometry shape = district.getMultiPolygon();
            Geometry boundingCircle = district.getBoundingCircle();
            return shape.getArea() / boundingCircle.getArea();
        }
    }

    class ConvexHull {
        static double computeCompactnessScore(DistrictNode district) {
            Geometry shape = district.getMultiPolygon();
            Geometry convexHull = district.getConvexHull();
            return shape.getArea() / convexHull.getArea();
        }
    }

    static double computeCompactnessScore(Compactness measure, DistrictNode district) {
        double compactnessScore;
        switch (measure) {
            case GRAPH_THEORETICAL:
                compactnessScore = GraphTheoretical.computeCompactnessScore(district);
                break;
            case POLSBY_POPPER:
                compactnessScore = PolsbyPopper.computeCompactnessScore(district);
                break;
            case SCHWARTZBERG:
                compactnessScore = Schwartzberg.computeCompactnessScore(district);
                break;
            case REOCK:
                compactnessScore = Reock.computeCompactnessScore(district);
                break;
            case CONVEX_HULL:
                compactnessScore = ConvexHull.computeCompactnessScore(district);
                break;
            default:
                throw new IllegalArgumentException("Replace this string later!");
        }
        return compactnessScore;
    }
}
