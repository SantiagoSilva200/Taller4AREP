package co.edu.arep;

import co.edu.arep.Annotations.Component;
import co.edu.arep.Annotations.RestController;
import co.edu.arep.Annotations.GetMapping;
import co.edu.arep.Annotations.RequestParam;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MicroSpringBoot {
    public static void main(String[] args) {
        try {
            exploreAndRegisterComponents();
            int port = 35000;
            String portEnv = System.getenv("PORT");
            if (portEnv != null) {
                try {
                    port = Integer.parseInt(portEnv);
                } catch (NumberFormatException e) {
                    System.out.println("Valor de PORT inválido, usando puerto por defecto 6000");
                }
            }
            HttpServer.runServer(args,port);

        } catch (Exception ex) {
            Logger.getLogger(MicroSpringBoot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void exploreAndRegisterComponents() throws Exception {
        registerIfComponent("co.edu.arep.HelloController");
        registerIfComponent("co.edu.arep.GreetingController");
    }

    private static void registerIfComponent(String className) {
        try {
            Class<?> clazz = Class.forName(className);

            if (clazz.isAnnotationPresent(Component.class) ||
                    clazz.isAnnotationPresent(RestController.class)) {

                Object instance = clazz.getDeclaredConstructor().newInstance();

                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(GetMapping.class)) {
                        GetMapping annotation = method.getAnnotation(GetMapping.class);
                        String path = annotation.value();

                        HttpServer.get(path, (req, resp) -> {
                            try {
                                Object[] params = getMethodParameters(method, req);
                                return (String) method.invoke(instance, params);
                            } catch (Exception e) {
                                return "Error: " + e.getMessage();
                            }
                        });
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error cargando clase " + className + ": " + e.getMessage());
        }
    }

    private static Object[] getMethodParameters(Method method, HttpRequest request) {
        Parameter[] parameters = method.getParameters();
        Object[] paramValues = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];

            if (param.isAnnotationPresent(RequestParam.class)) {
                RequestParam annotation = param.getAnnotation(RequestParam.class);
                String paramName = annotation.value();
                String defaultValue = annotation.defaultValue();

                String value = request.getValues(paramName);
                if (value == null || value.isEmpty()) {
                    value = defaultValue;
                }

                paramValues[i] = value;
            } else {
                paramValues[i] = "";
            }
        }

        return paramValues;
    }
}