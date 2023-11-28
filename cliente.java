import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class cliente {

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
                        consultarDatosCliente(conexion);
                        break;
                    case 2:
                        insertarDatosCliente(conexion);
                        break;
                    case 3:
                        actualizarDatosCliente(conexion);
                        break;
                    case 4:
                        eliminarDatosCliente(conexion);
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

    private static void consultarDatosCliente(Connection conexion) {
        String consultaSQL = "SELECT * FROM Cliente";
        try (PreparedStatement consulta = conexion.prepareStatement(consultaSQL);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {
                int idCliente = resultado.getInt("ID_cliente");
                String nombre = resultado.getString("Nombre");
                String direccion = resultado.getString("Direccion");
                String telefono = resultado.getString("Telefono");

                System.out.println("ID Cliente: " + idCliente);
                System.out.println("Nombre: " + nombre);
                System.out.println("Dirección: " + direccion);
                System.out.println("Teléfono: " + telefono);
                System.out.println("--------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertarDatosCliente(Connection conexion) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Ingrese los datos para la inserción del cliente:");

            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            System.out.print("Dirección: ");
            String direccion = scanner.nextLine();
            System.out.print("Teléfono: ");
            String telefono = scanner.nextLine();

            String consultaSQL = "INSERT INTO Cliente (Nombre, Direccion, Telefono) VALUES (?, ?, ?)";
            try (PreparedStatement consulta = conexion.prepareStatement(consultaSQL)) {
                consulta.setString(1, nombre);
                consulta.setString(2, direccion);
                consulta.setString(3, telefono);

                int filasAfectadas = consulta.executeUpdate();
                System.out.println(filasAfectadas + " fila(s) afectada(s).");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void actualizarDatosCliente(Connection conexion) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Ingrese el ID del cliente a actualizar:");
            int idCliente = scanner.nextInt();

            if (!existeCliente(conexion, idCliente)) {
                System.out.println("El cliente con ID " + idCliente + " no existe.");
                return;
            }

            System.out.println("Ingrese los nuevos datos para la actualización:");

            System.out.print("Nombre: ");
            String nombre = scanner.next();
            System.out.print("Dirección: ");
            String direccion = scanner.next();
            System.out.print("Teléfono: ");
            String telefono = scanner.next();

            String consultaSQL = "UPDATE Cliente SET Nombre = ?, Direccion = ?, Telefono = ? WHERE ID_cliente = ?";
            try (PreparedStatement consulta = conexion.prepareStatement(consultaSQL)) {
                consulta.setString(1, nombre);
                consulta.setString(2, direccion);
                consulta.setString(3, telefono);
                consulta.setInt(4, idCliente);

                int filasAfectadas = consulta.executeUpdate();
                System.out.println(filasAfectadas + " fila(s) afectada(s).");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void eliminarDatosCliente(Connection conexion) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Ingrese el ID del cliente a eliminar:");
            int idCliente = scanner.nextInt();

            if (!existeCliente(conexion, idCliente)) {
                System.out.println("El cliente con ID " + idCliente + " no existe.");
                return;
            }

            String consultaSQL = "DELETE FROM Cliente WHERE ID_cliente = ?";
            try (PreparedStatement consulta = conexion.prepareStatement(consultaSQL)) {
                consulta.setInt(1, idCliente);

                int filasAfectadas = consulta.executeUpdate();
                System.out.println(filasAfectadas + " fila(s) afectada(s).");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean existeCliente(Connection conexion, int idCliente) {
        String consultaSQL = "SELECT 1 FROM Cliente WHERE ID_cliente = ?";
        try (PreparedStatement consulta = conexion.prepareStatement(consultaSQL)) {
            consulta.setInt(1, idCliente);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
