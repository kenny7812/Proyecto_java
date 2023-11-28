import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class venta {

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
                        consultarDatosVenta(conexion);
                        break;
                    case 2:
                        insertarDatosVenta(conexion);
                        break;
                    case 3:
                        actualizarDatosVenta(conexion);
                        break;
                    case 4:
                        eliminarDatosVenta(conexion);
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

    private static void consultarDatosVenta(Connection conexion) {
        String consultaSQL = "SELECT * FROM Venta";
        try (PreparedStatement consulta = conexion.prepareStatement(consultaSQL);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {
                int idVenta = resultado.getInt("ID_venta");
                String fechaVenta = resultado.getString("Fecha_Venta");
                String numeroFactura = resultado.getString("Numero_Factura");
                int idProducto = resultado.getInt("Producto_ID");
                int idCliente = resultado.getInt("Cliente_ID");
                int cantidadVendida = resultado.getInt("Cantidad_Vendida");
                double precioUnitario = resultado.getDouble("Precio_Unitario");

                System.out.println("ID Venta: " + idVenta);
                System.out.println("Fecha de Venta: " + fechaVenta);
                System.out.println("Número de Factura: " + numeroFactura);
                System.out.println("ID Producto: " + idProducto);
                System.out.println("ID Cliente: " + idCliente);
                System.out.println("Cantidad Vendida: " + cantidadVendida);
                System.out.println("Precio Unitario: " + precioUnitario);
                System.out.println("--------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertarDatosVenta(Connection conexion) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Ingrese los datos para la inserción de la venta:");

            System.out.print("Fecha de Venta (YYYY-MM-DD): ");
            String fechaVenta = scanner.next();
            System.out.print("Número de Factura: ");
            String numeroFactura = scanner.next();
            System.out.print("ID Producto: ");
            int idProducto = scanner.nextInt();
            System.out.print("ID Cliente: ");
            int idCliente = scanner.nextInt();
            System.out.print("Cantidad Vendida: ");
            int cantidadVendida = scanner.nextInt();
            System.out.print("Precio Unitario: ");
            double precioUnitario = scanner.nextDouble();

            String consultaSQL = "INSERT INTO Venta (Fecha_Venta, Numero_Factura, Producto_ID, Cliente_ID, Cantidad_Vendida, Precio_Unitario) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement consulta = conexion.prepareStatement(consultaSQL)) {
                consulta.setString(1, fechaVenta);
                consulta.setString(2, numeroFactura);
                consulta.setInt(3, idProducto);
                consulta.setInt(4, idCliente);
                consulta.setInt(5, cantidadVendida);
                consulta.setDouble(6, precioUnitario);

                int filasAfectadas = consulta.executeUpdate();
                System.out.println(filasAfectadas + " fila(s) afectada(s).");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void actualizarDatosVenta(Connection conexion) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Ingrese el ID de la venta a actualizar:");
            int idVenta = scanner.nextInt();

            if (!existeVenta(conexion, idVenta)) {
                System.out.println("La venta con ID " + idVenta + " no existe.");
                return;
            }

            System.out.println("Ingrese los nuevos datos para la actualización:");

            System.out.print("Fecha de Venta (YYYY-MM-DD): ");
            String fechaVenta = scanner.next();
            System.out.print("Número de Factura: ");
            String numeroFactura = scanner.next();
            System.out.print("ID Producto: ");
            int idProducto = scanner.nextInt();
            System.out.print("ID Cliente: ");
            int idCliente = scanner.nextInt();
            System.out.print("Cantidad Vendida: ");
            int cantidadVendida = scanner.nextInt();
            System.out.print("Precio Unitario: ");
            double precioUnitario = scanner.nextDouble();

            String consultaSQL = "UPDATE Venta SET Fecha_Venta = ?, Numero_Factura = ?, Producto_ID = ?, Cliente_ID = ?, Cantidad_Vendida = ?, Precio_Unitario = ? WHERE ID_venta = ?";
            try (PreparedStatement consulta = conexion.prepareStatement(consultaSQL)) {
                consulta.setString(1, fechaVenta);
                consulta.setString(2, numeroFactura);
                consulta.setInt(3, idProducto);
                consulta.setInt(4, idCliente);
                consulta.setInt(5, cantidadVendida);
                consulta.setDouble(6, precioUnitario);
                consulta.setInt(7, idVenta);

                int filasAfectadas = consulta.executeUpdate();
                System.out.println(filasAfectadas + " fila(s) afectada(s).");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void eliminarDatosVenta(Connection conexion) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Ingrese el ID de la venta a eliminar:");
            int idVenta = scanner.nextInt();

            if (!existeVenta(conexion, idVenta)) {
                System.out.println("La venta con ID " + idVenta + " no existe.");
                return;
            }

            String consultaSQL = "DELETE FROM Venta WHERE ID_venta = ?";
            try (PreparedStatement consulta = conexion.prepareStatement(consultaSQL)) {
                consulta.setInt(1, idVenta);

                int filasAfectadas = consulta.executeUpdate();
                System.out.println(filasAfectadas + " fila(s) afectada(s).");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean existeVenta(Connection conexion, int idVenta) {
        String consultaSQL = "SELECT 1 FROM Venta WHERE ID_venta = ?";
        try (PreparedStatement consulta = conexion.prepareStatement(consultaSQL)) {
            consulta.setInt(1, idVenta);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
