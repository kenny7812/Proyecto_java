import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class compra {

    private static final String URL = "jdbc:mysql://localhost:3306/Sistema_Inventario";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "100122";

    public static void main(String[] args) {
        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
             Scanner scanner = new Scanner(System.in)) {

            while (true) {
                System.out.println("Seleccione una operación:");
                System.out.println("1. Consulta Compras");
                System.out.println("2. Inserción Compra");
                System.out.println("3. Actualización Compra");
                System.out.println("4. Eliminación Compra");
                System.out.println("0. Salir");

                int opcion = scanner.nextInt();

                switch (opcion) {
                    case 1:
                        consultarCompras(conexion);
                        break;
                    case 2:
                        insertarCompra(conexion);
                        break;
                    case 3:
                        actualizarCompra(conexion);
                        break;
                    case 4:
                        eliminarCompra(conexion);
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

    private static void consultarCompras(Connection conexion) {
        String consultaSQL = "SELECT * FROM Compra";
        try (PreparedStatement consulta = conexion.prepareStatement(consultaSQL);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {
                int idCompra = resultado.getInt("ID_Compra");
                java.sql.Date fechaCompra = resultado.getDate("Fecha_Compra");
                String numeroFactura = resultado.getString("Numero_Factura");
                int cantidadComprada = resultado.getInt("Cantidad_Comprada");
                double precioUnitario = resultado.getDouble("Precio_Unitario");
                int productoID = resultado.getInt("Producto_ID");
                int proveedorID = resultado.getInt("Proveedor_ID");

                System.out.println("ID Compra: " + idCompra);
                System.out.println("Fecha de Compra: " + fechaCompra);
                System.out.println("Número de Factura: " + numeroFactura);
                System.out.println("Cantidad Comprada: " + cantidadComprada);
                System.out.println("Precio Unitario: " + precioUnitario);
                System.out.println("ID Producto: " + productoID);
                System.out.println("ID Proveedor: " + proveedorID);
                System.out.println("--------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertarCompra(Connection conexion) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Ingrese los datos para la inserción de Compra:");

            System.out.print("Fecha de Compra (formato YYYY-MM-DD): ");
            String fechaCompraStr = scanner.nextLine();
            System.out.print("Número de Factura: ");
            String numeroFactura = scanner.nextLine();
            System.out.print("Cantidad Comprada: ");
            int cantidadComprada = scanner.nextInt();
            System.out.print("Precio Unitario: ");
            double precioUnitario = scanner.nextDouble();
            System.out.print("ID Producto: ");
            int productoID = scanner.nextInt();
            System.out.print("ID Proveedor: ");
            int proveedorID = scanner.nextInt();

            // Convertir la cadena de fecha a un objeto java.sql.Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date fechaCompraUtil = dateFormat.parse(fechaCompraStr);
            java.sql.Date fechaCompraSQL = new java.sql.Date(fechaCompraUtil.getTime());

            String consultaSQL = "INSERT INTO Compra (Fecha_Compra, Numero_Factura, Cantidad_Comprada, Precio_Unitario, Producto_ID, Proveedor_ID) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement consulta = conexion.prepareStatement(consultaSQL)) {
                consulta.setDate(1, fechaCompraSQL);
                consulta.setString(2, numeroFactura);
                consulta.setInt(3, cantidadComprada);
                consulta.setDouble(4, precioUnitario);
                consulta.setInt(5, productoID);
                consulta.setInt(6, proveedorID);

                int filasAfectadas = consulta.executeUpdate();
                System.out.println(filasAfectadas + " fila(s) afectada(s).");
            }

        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static void actualizarCompra(Connection conexion) {
        // Implementa la actualización de compras similar a la de productos
        // ...
    }

    private static void eliminarCompra(Connection conexion) {
        // Implementa la eliminación de compras similar a la de productos
        // ...
    }
}
