import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class inventario {

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
                        consultarDatosInventario(conexion);
                        break;
                    case 2:
                        insertarDatosInventario(conexion);
                        break;
                    case 3:
                        actualizarDatosInventario(conexion);
                        break;
                    case 4:
                        eliminarDatosInventario(conexion);
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

    private static void consultarDatosInventario(Connection conexion) {
        String consultaSQL = "SELECT * FROM Inventario";
        try (PreparedStatement consulta = conexion.prepareStatement(consultaSQL);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {
                int idInventario = resultado.getInt("ID_inventario");
                String fechaRegistro = resultado.getString("Fecha_Registro");
                String tipoMovimiento = resultado.getString("Tipo_Movimiento");
                int idProducto = resultado.getInt("Producto_ID");
                int cantidad = resultado.getInt("Cantidad");

                System.out.println("ID Inventario: " + idInventario);
                System.out.println("Fecha de Registro: " + fechaRegistro);
                System.out.println("Tipo de Movimiento: " + tipoMovimiento);
                System.out.println("ID Producto: " + idProducto);
                System.out.println("Cantidad: " + cantidad);
                System.out.println("--------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertarDatosInventario(Connection conexion) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Ingrese los datos para la inserción en el inventario:");

            System.out.print("Fecha de Registro (YYYY-MM-DD): ");
            String fechaRegistro = scanner.next();
            System.out.print("Tipo de Movimiento (Entrada/Salida): ");
            String tipoMovimiento = scanner.next();
            System.out.print("ID Producto: ");
            int idProducto = scanner.nextInt();
            System.out.print("Cantidad: ");
            int cantidad = scanner.nextInt();

            String consultaSQL = "INSERT INTO Inventario (Fecha_Registro, Tipo_Moviento, Producto_ID, Cantidad) VALUES (?, ?, ?, ?)";
            try (PreparedStatement consulta = conexion.prepareStatement(consultaSQL)) {
                consulta.setString(1, fechaRegistro);
                consulta.setString(2, tipoMovimiento);
                consulta.setInt(3, idProducto);
                consulta.setInt(4, cantidad);

                int filasAfectadas = consulta.executeUpdate();
                System.out.println(filasAfectadas + " fila(s) afectada(s).");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void actualizarDatosInventario(Connection conexion) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Ingrese el ID del inventario a actualizar:");
            int idInventario = scanner.nextInt();

            if (!existeInventario(conexion, idInventario)) {
                System.out.println("El registro de inventario con ID " + idInventario + " no existe.");
                return;
            }

            System.out.println("Ingrese los nuevos datos para la actualización:");

            System.out.print("Fecha de Registro (YYYY-MM-DD): ");
            String fechaRegistro = scanner.next();
            System.out.print("Tipo de Movimiento (Entrada/Salida): ");
            String tipoMovimiento = scanner.next();
            System.out.print("ID Producto: ");
            int idProducto = scanner.nextInt();
            System.out.print("Cantidad: ");
            int cantidad = scanner.nextInt();

            String consultaSQL = "UPDATE Inventario SET Fecha_Registro = ?, Tipo_Moviento = ?, Producto_ID = ?, Cantidad = ? WHERE ID_inventario = ?";
            try (PreparedStatement consulta = conexion.prepareStatement(consultaSQL)) {
                consulta.setString(1, fechaRegistro);
                consulta.setString(2, tipoMovimiento);
                consulta.setInt(3, idProducto);
                consulta.setInt(4, cantidad);
                consulta.setInt(5, idInventario);

                int filasAfectadas = consulta.executeUpdate();
                System.out.println(filasAfectadas + " fila(s) afectada(s).");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void eliminarDatosInventario(Connection conexion) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Ingrese el ID del inventario a eliminar:");
            int idInventario = scanner.nextInt();

            if (!existeInventario(conexion, idInventario)) {
                System.out.println("El registro de inventario con ID " + idInventario + " no existe.");
                return;
            }

            String consultaSQL = "DELETE FROM Inventario WHERE ID_inventario = ?";
            try (PreparedStatement consulta = conexion.prepareStatement(consultaSQL)) {
                consulta.setInt(1, idInventario);

                int filasAfectadas = consulta.executeUpdate();
                System.out.println(filasAfectadas + " fila(s) afectada(s).");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean existeInventario(Connection conexion, int idInventario) {
        String consultaSQL = "SELECT 1 FROM Inventario WHERE ID_inventario = ?";
        try (PreparedStatement consulta = conexion.prepareStatement(consultaSQL)) {
            consulta.setInt(1, idInventario);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
