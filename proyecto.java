import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class proyecto {

    private static final String URL = "jdbc:mysql://localhost:3306/Sistema_Inventario";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "100122";

    public static void main(String[] args) {
        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
             Scanner scanner = new Scanner(System.in)) {

            while (true) {
                System.out.println("Seleccione una operación:");
                System.out.println("1. Consulta");
                System.out.println("2. Inserción");
                System.out.println("3. Actualización");
                System.out.println("4. Eliminación");
                System.out.println("0. Salir");

                int opcion = scanner.nextInt();

                switch (opcion) {
                    case 1:
                        consultarDatos(conexion);
                        break;
                    case 2:
                        insertarDatos(conexion);
                        break;
                    case 3:
                        actualizarDatos(conexion);
                        break;
                    case 4:
                        eliminarDatos(conexion);
                        break;
                    case 0:
                        System.out.println("Saliendo del programa.");
                        System.exit(0);
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                        break;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void consultarDatos(Connection conexion) {
        String consultaSQL = "SELECT * FROM Producto";
        try (PreparedStatement consulta = conexion.prepareStatement(consultaSQL);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {
                int idProducto = resultado.getInt("ID_producto");
                String nombre = resultado.getString("Nombre");
                String descripcion = resultado.getString("Descripcion");
                double precio = resultado.getDouble("Precio");
                int cantidadStock = resultado.getInt("Cantidad_Stock");

                System.out.println("ID Producto: " + idProducto);
                System.out.println("Nombre: " + nombre);
                System.out.println("Descripción: " + descripcion);
                System.out.println("Precio: " + precio);
                System.out.println("Cantidad en Stock: " + cantidadStock);
                System.out.println("--------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertarDatos(Connection conexion) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Ingrese los datos para la inserción:");

            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            System.out.print("Descripción: ");
            String descripcion = scanner.nextLine();
            System.out.print("Precio: ");
            double precio = scanner.nextDouble();
            System.out.print("Cantidad en Stock: ");
            int cantidadStock = scanner.nextInt();

            String consultaSQL = "INSERT INTO Producto (Nombre, Descripcion, Precio, Cantidad_Stock) VALUES (?, ?, ?, ?)";
            try (PreparedStatement consulta = conexion.prepareStatement(consultaSQL)) {
                consulta.setString(1, nombre);
                consulta.setString(2, descripcion);
                consulta.setDouble(3, precio);
                consulta.setInt(4, cantidadStock);

                int filasAfectadas = consulta.executeUpdate();
                System.out.println(filasAfectadas + " fila(s) afectada(s).");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void actualizarDatos(Connection conexion) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Ingrese el ID del producto a actualizar:");
            int idProducto = scanner.nextInt();

            if (!existeProducto(conexion, idProducto)) {
                System.out.println("El producto con ID " + idProducto + " no existe.");
                return;
            }

            System.out.println("Ingrese los nuevos datos para la actualización:");

            System.out.print("Nombre: ");
            String nombre = scanner.next();
            System.out.print("Descripción: ");
            String descripcion = scanner.next();
            System.out.print("Precio: ");
            double precio = scanner.nextDouble();
            System.out.print("Cantidad en Stock: ");
            int cantidadStock = scanner.nextInt();

            String consultaSQL = "UPDATE Producto SET Nombre = ?, Descripcion = ?, Precio = ?, Cantidad_Stock = ? WHERE ID_producto = ?";
            try (PreparedStatement consulta = conexion.prepareStatement(consultaSQL)) {
                consulta.setString(1, nombre);
                consulta.setString(2, descripcion);
                consulta.setDouble(3, precio);
                consulta.setInt(4, cantidadStock);
                consulta.setInt(5, idProducto);

                int filasAfectadas = consulta.executeUpdate();
                System.out.println(filasAfectadas + " fila(s) afectada(s).");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void eliminarDatos(Connection conexion) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Ingrese el ID del producto a eliminar:");
            int idProducto = scanner.nextInt();

            if (!existeProducto(conexion, idProducto)) {
                System.out.println("El producto con ID " + idProducto + " no existe.");
                return;
            }

            String consultaSQL = "DELETE FROM Producto WHERE ID_producto = ?";
            try (PreparedStatement consulta = conexion.prepareStatement(consultaSQL)) {
                consulta.setInt(1, idProducto);

                int filasAfectadas = consulta.executeUpdate();
                System.out.println(filasAfectadas + " fila(s) afectada(s).");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean existeProducto(Connection conexion, int idProducto) {
        String consultaSQL = "SELECT 1 FROM Producto WHERE ID_producto = ?";
        try (PreparedStatement consulta = conexion.prepareStatement(consultaSQL)) {
            consulta.setInt(1, idProducto);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
