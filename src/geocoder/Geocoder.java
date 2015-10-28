package geocoder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Double.parseDouble;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Geocoder {
    
    public static String[]  municipios = { 
        "alaro", "alcudia", "algaida", "andratx", "ariany", "arta", "banyalbufar", "binissalem",
        "buger", "bunyola", "cabrera", "calvia", "campanet", "campos", "capdepera", "consell",
        "costitx", "deia", "escorca", "esporles", "estellencs", "felanitx", "fornalutx", "inca",
        "la_dragonera", "lloret", "lloseta", "llubi", "llucmajor", "manacor", "mancor_dela_vall",
        "maria_dela_salut", "marratxi", "montuiri", "muro", "palma", "petra", "pollença", "porreres",
        "puigpunyent", "sa_pobla", "sant_joan", "sant_llorenç_des_cardassar", "santa_eugenia", 
        "santa_margalida", "santa_maria_del_cami", "santanyi", "selva", "sencelles", "ses_salines",
        "sineu", "soller", "son_servera", "valldemossa", "vilafranca" 
    };
    
    public static ArrayList<Point> readGPX(String path_gpx) {
        ArrayList<Point> points = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(path_gpx));        
            String linea = br.readLine();
            while (linea != null) {
                if (linea.contains("<trkpt")) {     
                    String trozos[] = linea.split("=");
                    String trozos_lat[] = trozos[1].split("\"");
                    String trozos_lon[] = trozos[2].split("\"");
                    points.add(new Point(parseDouble(trozos_lat[1]), parseDouble(trozos_lon[1])));
                }
                linea = br.readLine();
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Point.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Point.class.getName()).log(Level.SEVERE, null, ex);
        }
        return points;
    }
    
    public static boolean inside(Point point, ArrayList<Point> polygon) {
        for (Point vertex : polygon) if (point.equals(vertex)) return true;
        int n_intersections = 0;
        int n_vertex = polygon.size();
        for (int i = 1; i < n_vertex; i++) {
            Point vertex1 = polygon.get(i-1);
            Point vertex2 = polygon.get(i);
            if ((vertex1.getY() == vertex2.getY()) && 
                (vertex1.getY() == point.getY()) &&
                (point.getX() > Math.min(vertex1.getX(), vertex2.getX())) &&
                (point.getX() < Math.max(vertex1.getX(), vertex2.getX()))    
               ) return true;
            if ((point.getY() > Math.min(vertex1.getY(), vertex2.getY())) &&
                (point.getY() <= Math.max(vertex1.getY(), vertex2.getY())) &&
                (point.getX() <= Math.max(vertex1.getX(), vertex2.getX())) && 
                (vertex1.getY() != vertex2.getY())
               ) {
                double xinters = (point.getY()-vertex1.getY()) * (vertex2.getX() - vertex1.getX()) 
                                    / (vertex2.getY() - vertex1.getY()) + vertex1.getX();
                if (xinters == point.getX()) return true;
                if (vertex1.getX() == vertex2.getX() || point.getX() <= xinters) n_intersections++;
            }      
        }
        if (n_intersections%2 != 0) return true; else return false;
    }
    
    public static String getMunicipio(Point point) {
        int n_municipios = municipios.length;
        int i = 0;
        boolean find = false;
        while (i < n_municipios && !find) {
            ArrayList<Point> municipio = readGPX("municipios/" + municipios[i] + ".gpx");
            if (inside(point, municipio)) find = true; else i++;
        }
        if (find) return municipios[i]; else return "external";
    }

    public static void main(String[] args) {
        Point point = new Point(39.910404, 3.066487);
        System.out.println(getMunicipio(point));
    }
    
}
