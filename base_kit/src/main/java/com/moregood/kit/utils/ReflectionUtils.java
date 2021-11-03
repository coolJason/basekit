package com.moregood.kit.utils;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * @类名 ReflectionUtils
 * @描述
 * @作者 xifengye
 * @创建时间 2020/11/23 09:59
 * @邮箱 ye_xi_feng@163.com
 */
public class ReflectionUtils {


    /**
     * 获取包名
     *
     * @return 包名【String类型】
     */
    public static String getPackage(Class<?> clazz) {
        Package pck = clazz.getPackage();
        if (null != pck) {
            return pck.getName();
        } else {
            return "没有包！";
        }
    }

    /**
     * 获取继承的父类的全类名
     *
     * @return 继承的父类的全类名【String类型】
     */
    public static String getSuperClassName(Class<?> clazz) {
        Class<?> superClass = clazz.getSuperclass();
        if (null != superClass) {
            return superClass.getName();
        } else {
            return "没有父类！";
        }
    }

    /**
     * 获取全类名
     *
     * @return 全类名【String类型】
     */
    public static String getClassName(Class<?> clazz) {
        return clazz.getName();
    }

    /**
     * 获取实现的接口名
     *
     * @return 所有的接口名【每一个接口名的类型为String，最后保存到一个List集合中】
     */
    public static List<String> getInterfaces(Class<?> clazz) {
        Class<?>[] interfaces = clazz.getInterfaces();
        int len = interfaces.length;

        List<String> list = new ArrayList<String>();
        for (int i = 0; i < len; i++) {
            Class<?> itfc = interfaces[i];

            // 接口名
            String interfaceName = itfc.getSimpleName();

            list.add(interfaceName);
        }

        return list;
    }

    /**
     * 获取所有属性
     *
     * @return 所有的属性【每一个属性添加到StringBuilder中，最后保存到一个List集合中】
     */
    public static List<StringBuilder> getFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        int len = fields.length;

        List<StringBuilder> list = new ArrayList<StringBuilder>();
        StringBuilder sb = null;
        for (int i = 0; i < len; i++) {
            Field field = fields[i];
            sb = new StringBuilder();

            // 修饰符
            String modifier = Modifier.toString(field.getModifiers());
            sb.append(modifier + " ");

            // 数据类型
            Class<?> type = field.getType();
            String typeName = type.getSimpleName();
            sb.append(typeName + " ");

            // 属性名
            String fieldName = field.getName();
            sb.append(fieldName + ";");

            list.add(sb);
        }

        return list;
    }

    /**
     * 获取所有公共的属性
     *
     * @return 所有公共的属性【每一个属性添加到StringBuilder中，最后保存到一个List集合中】
     */
    public static List<StringBuilder> getPublicFields(Class<?> clazz) {
        Field[] fields = clazz.getFields();
        int len = fields.length;

        List<StringBuilder> list = new ArrayList<StringBuilder>();
        StringBuilder sb = null;
        for (int i = 0; i < len; i++) {
            Field field = fields[i];
            sb = new StringBuilder();

            // 修饰符
            String modifier = Modifier.toString(field.getModifiers());
            sb.append(modifier + " ");

            // 数据类型
            Class<?> type = field.getType();
            String typeName = type.getSimpleName();
            sb.append(typeName + " ");

            // 属性名
            String fieldName = field.getName();
            sb.append(fieldName + ";");

            list.add(sb);
        }

        return list;
    }

    /**
     * 获取所有构造方法
     *
     * @return 所有的构造方法【每一个构造方法添加到StringBuilder中，最后保存到一个List集合中】
     */
    public static List<StringBuilder> getConstructors(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        int len = constructors.length;

        List<StringBuilder> list = new ArrayList<StringBuilder>();
        StringBuilder sb = null;
        for (int i = 0; i < len; i++) {
            Constructor<?> constructor = constructors[i];
            sb = new StringBuilder();

            // 修饰符
            String modifier = Modifier.toString(constructor.getModifiers());
            sb.append(modifier + " ");

            // 方法名（类名）
            String constructorName = clazz.getSimpleName();
            sb.append(constructorName + " (");

            // 形参列表
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            int length = parameterTypes.length;
            for (int j = 0; j < length; j++) {
                Class<?> parameterType = parameterTypes[j];

                String parameterTypeName = parameterType.getSimpleName();

                if (j < length - 1) {
                    sb.append(parameterTypeName + ", ");
                } else {
                    sb.append(parameterTypeName);
                }

            }

            sb.append(") {}");

            list.add(sb);
        }

        return list;
    }

    /**
     * 获取所有自身的方法
     *
     * @return 所有自身的方法【每一个方法添加到StringBuilder中，最后保存到一个List集合中】
     */
    public static List<StringBuilder> getMethods(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        int len = methods.length;

        List<StringBuilder> list = new ArrayList<StringBuilder>();
        StringBuilder sb = null;
        for (int i = 0; i < len; i++) {
            Method method = methods[i];
            sb = new StringBuilder();

            // 修饰符
            String modifier = Modifier.toString(method.getModifiers());
            sb.append(modifier + " ");

            // 返回值类型
            Class<?> returnClass = method.getReturnType();
            String returnType = returnClass.getSimpleName();
            sb.append(returnType + " ");

            // 方法名
            String methodName = method.getName();
            sb.append(methodName + " (");

            // 形参列表
            Class<?>[] parameterTypes = method.getParameterTypes();
            int length = parameterTypes.length;

            for (int j = 0; j < length; j++) {
                Class<?> parameterType = parameterTypes[j];

                // 形参类型
                String parameterTypeName = parameterType.getSimpleName();

                if (j < length - 1) {
                    sb.append(parameterTypeName + ", ");
                } else {
                    sb.append(parameterTypeName);
                }

            }

            sb.append(") {}");

            list.add(sb);
        }

        return list;
    }

    /**
     * 获取所有公共的方法
     *
     * @return 所有公共的方法【每一个方法添加到StringBuilder中，最后保存到一个List集合中】
     */
    public static List<StringBuilder> getPublicMethods(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        int len = methods.length;

        List<StringBuilder> list = new ArrayList<StringBuilder>();
        StringBuilder sb = null;
        for (int i = 0; i < len; i++) {
            Method method = methods[i];
            sb = new StringBuilder();

            // 修饰符
            String modifier = Modifier.toString(method.getModifiers());
            sb.append(modifier + " ");

            // 返回值类型
            Class<?> returnClass = method.getReturnType();
            String returnType = returnClass.getSimpleName();
            sb.append(returnType + " ");

            // 方法名
            String methodName = method.getName();
            sb.append(methodName + " (");

            // 形参列表
            Class<?>[] parameterTypes = method.getParameterTypes();
            int length = parameterTypes.length;

            for (int j = 0; j < length; j++) {
                Class<?> parameterType = parameterTypes[j];

                // 形参类型
                String parameterTypeName = parameterType.getSimpleName();

                if (j < length - 1) {
                    sb.append(parameterTypeName + ", ");
                } else {
                    sb.append(parameterTypeName);
                }

            }

            sb.append(") {}");

            list.add(sb);
        }

        return list;
    }

    /**
     * 获取所有的注解名
     *
     * @return 所有的注解名【每一个注解名的类型为String，最后保存到一个List集合中】
     */
    public static List<String> getAnnotations(Class<?> clazz) {
        Annotation[] annotations = clazz.getAnnotations();
        int len = annotations.length;

        List<String> list = new ArrayList<String>();
        for (int i = 0; i < len; i++) {
            Annotation annotation = annotations[i];

            String annotationName = annotation.annotationType().getSimpleName();
            list.add(annotationName);
        }

        return list;
    }

    /**
     * 获取父类的泛型
     *
     * @return 父类的泛型【Class类型】
     */
    public static Class<?> getSuperClassGenericParameterizedType(Class<?> clazz) {
        Type genericSuperClass = clazz.getGenericSuperclass();

        Class<?> superClassGenericParameterizedType = null;

        // 判断父类是否有泛型
        if (genericSuperClass instanceof ParameterizedType) {
            // 向下转型，以便调用方法
            ParameterizedType pt = (ParameterizedType) genericSuperClass;
            // 只取第一个，因为一个类只能继承一个父类
            Type superClazz = pt.getActualTypeArguments()[0];
            // 转换为Class类型
            superClassGenericParameterizedType = (Class<?>) superClazz;
        }

        return superClassGenericParameterizedType;
    }

    /**
     * 获取接口的所有泛型
     *
     * @return 所有的泛型接口【每一个泛型接口的类型为Class，最后保存到一个List集合中】
     */
    public static List<Class<?>> getInterfaceGenericParameterizedTypes(Class<?> clazz) {
        Type[] genericInterfaces = clazz.getGenericInterfaces();
        int len = genericInterfaces.length;

        List<Class<?>> list = new ArrayList<Class<?>>();
        for (int i = 0; i < len; i++) {
            Type genericInterface = genericInterfaces[i];

            // 判断接口是否有泛型
            if (genericInterface instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) genericInterface;

                // 得到所有的泛型【Type类型的数组】
                Type[] interfaceTypes = pt.getActualTypeArguments();

                int length = interfaceTypes.length;

                for (int j = 0; j < length; j++) {
                    // 获取对应的泛型【Type类型】
                    Type interfaceType = interfaceTypes[j];
                    // 转换为Class类型
                    Class<?> interfaceClass = (Class<?>) interfaceType;
                    list.add(interfaceClass);
                }

            }

        }

        return list;
    }

    /**
     * 打印包名
     */
    public static void printPackage(Class<?> clazz) {
        System.out.println(getPackage(clazz));
    }

    /**
     * 打印继承的父类的全类名
     */
    public static void printSuperClassName(Class<?> clazz) {
        System.out.println(getSuperClassName(clazz));
    }

    /**
     * 打印全类名
     */
    public static void printClassName(Class<?> clazz) {
        System.out.println(getClassName(clazz));
    }

    /**
     * 打印实现的接口
     */
    public static void printInterfaces(Class<?> clazz) {
        List<String> list = getInterfaces(clazz);
        int size = list.size();
        if (0 < size) {
            for (int i = 0; i < size; i++) {
                System.out.println(list.get(i));
            }
        } else {
            System.out.println("没有实现接口！");
        }
    }

    /**
     * 打印所有属性
     */
    public static void printFields(Class<?> clazz) {
        List<StringBuilder> list = getFields(clazz);
        int size = list.size();
        if (0 < size) {
            for (int i = 0; i < size; i++) {
                System.out.println(list.get(i));
            }
        } else {
            System.out.println("没有属性！");
        }
    }

    /**
     * 打印所有公共的属性
     */
    public static void printPublicFields(Class<?> clazz) {
        List<StringBuilder> list = getPublicFields(clazz);
        int size = list.size();
        if (0 < size) {
            for (int i = 0; i < size; i++) {
                System.out.println(list.get(i));
            }
        } else {
            System.out.println("没有属性！");
        }
    }

    /**
     * 打印所有构造方法
     */
    public static void printConstructors(Class<?> clazz) {
        List<StringBuilder> list = getConstructors(clazz);
        int size = list.size();
        if (0 < size) {
            for (int i = 0; i < size; i++) {
                System.out.println(list.get(i));
            }
        } else {
            System.out.println("没有构造方法！");
        }
    }

    /**
     * 打印所有方法
     */
    public static void printMethods(Class<?> clazz) {
        List<StringBuilder> list = getMethods(clazz);
        int size = list.size();
        if (0 < size) {
            for (int i = 0; i < size; i++) {
                System.out.println(list.get(i));
            }
        } else {
            System.out.println("没有方法！");
        }
    }

    /**
     * 打印所有公共的方法
     */
    public static void printPublicMethods(Class<?> clazz) {
        List<StringBuilder> list = getPublicMethods(clazz);
        int size = list.size();
        if (0 < size) {
            for (int i = 0; i < size; i++) {
                System.out.println(list.get(i));
            }
        } else {
            System.out.println("没有方法！");
        }
    }

    /**
     * 打印所有的注解
     */
    public static void printAnnotations(Class<?> clazz) {
        List<String> list = getAnnotations(clazz);
        int size = list.size();
        if (0 < size) {
            for (int i = 0; i < size; i++) {
                System.out.println(list.get(i));
            }
        } else {
            System.out.println("没有注解！");
        }
    }

    /**
     * 打印父类的泛型名
     */
    public static void printSuperClassGenericParameterizedType(Class<?> clazz) {
        Class<?> superClassGenericParameterizedType = getSuperClassGenericParameterizedType(clazz);
        if (null != superClassGenericParameterizedType) {
            System.out.println(superClassGenericParameterizedType.getSimpleName());
        } else {
            System.out.println("父类没有泛型！");
        }
    }

    /**
     * 打印接口的泛型
     */
    public static void printInterfaceGenericParameterizedTypes(Class<?> clazz) {
        List<Class<?>> list = getInterfaceGenericParameterizedTypes(clazz);
        int size = list.size();
        if (0 < size) {
            for (int i = 0; i < size; i++) {
                System.out.println(list.get(i).getSimpleName());
            }
        } else {
            System.out.println("没有泛型接口！");
        }
    }

    /**
     * 打印一个类的相关信息
     *
     * @param clazz
     */
    public static void printAll(Class<?> clazz) {
        System.out.print("【包名】  ");
        printPackage(clazz);

        System.out.print("【类名】  ");
        System.out.println(clazz.getSimpleName());

        System.out.println("\n【父类全类名】");
        printSuperClassName(clazz);
        System.out.println("【全类名】");
        printClassName(clazz);
        System.out.println("\n【所有已实现的接口】");
        printInterfaces(clazz);

        System.out.println("\n【属性】");
        printFields(clazz);
        System.out.println("\n【构造方法】");
        printConstructors(clazz);
        System.out.println("\n【方法】");
        printMethods(clazz);

        System.out.println("\n【公共的属性】");
        printPublicFields(clazz);
        System.out.println("\n【公共的方法】");
        printPublicMethods(clazz);
    }

    /**
     * 根据Class类型，获取对应的实例【要求必须有无参的构造器】
     *
     * @return 对应的实例【Object类型】
     */
    public static Object getNewInstance(Class<?> clazz) throws InstantiationException, IllegalAccessException {
        return clazz.newInstance();
    }

    /**
     * 根据传入的类的Class对象，以及构造方法的形参的Class对象，获取对应的构造方法对象
     *
     * @param clazz          类的Class对象
     * @param parameterTypes 构造方法的形参的Class对象【可以不写】
     * @return 构造方法对象【Constructor类型】
     */
    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... parameterTypes)
            throws NoSuchMethodException, SecurityException {
        return clazz.getDeclaredConstructor(parameterTypes);
    }

    /**
     * 根据传入的构造方法对象，以及，获取对应的实例
     *
     * @param constructor 构造方法对象
     * @param initargs    传入构造方法的实参【可以不写】
     * @return 对应的实例【Object类型】
     */
    public static Object getNewInstance(Constructor<?> constructor, Object... initargs)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        constructor.setAccessible(true);
        return constructor.newInstance(initargs);
    }

    /**
     * 根据传入的属性名字符串，修改对应的属性值
     *
     * @param clazz 类的Class对象
     * @param name  属性名
     * @param obj   要修改的实例对象
     * @param value 修改后的新值
     */
    public static void setField(Class<?> clazz, String name, Object obj, Object value)
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        field.set(obj, value);
    }

    /**
     * 根据传入的方法名字符串，获取对应的方法
     *
     * @param clazz          类的Class对象
     * @param name           方法名
     * @param parameterTypes 方法的形参对应的Class类型【可以不写】
     * @return 方法对象【Method类型】
     */
    public static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes)
            throws NoSuchMethodException, SecurityException {
        return clazz.getDeclaredMethod(name, parameterTypes);
    }

    /**
     * 根据传入的方法对象，调用对应的方法
     *
     * @param method 方法对象
     * @param obj    要调用的实例对象【如果是调用静态方法，则可以传入null】
     * @param args   传入方法的实参【可以不写】
     * @return 方法的返回值【没有返回值，则返回null】
     */
    public static Object invokeMethod(Method method, Object obj, Object... args)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        method.setAccessible(true);
        return method.invoke(obj, args);
    }


    /**
     * 使用反射调用方法
     *
     * @param o          被调用对象
     * @param methodName 被调用对象的方法名称
     * @param args       被调用方法所需传入的参数列表
     */
    public static Object callMethod(Object o, String methodName, Object... args) {
        Object result = null;
        try {
            int argslen = args.length;
            Class c = o.getClass();
            Method m = null;

            Class<?> argsTypes[] = new Class<?>[argslen];
            //基本类型在参数传递过程中会自动封住成对象类型，这里还原成基本类型的Class
            for (int i = 0; i < argslen; i++) {
                argsTypes[i] = args[i].getClass();
                if (argsTypes[i] == Integer.class) {
                    argsTypes[i] = int.class;
                }
                if (argsTypes[i] == Float.class) {
                    argsTypes[i] = float.class;
                }
                if (argsTypes[i] == Long.class) {
                    argsTypes[i] = long.class;
                }
                if (argsTypes[i] == Byte.class) {
                    argsTypes[i] = byte.class;
                }
                if (argsTypes[i] == Double.class) {
                    argsTypes[i] = double.class;
                }
                if (argsTypes[i] == Boolean.class) {
                    argsTypes[i] = boolean.class;
                }
            }

            if (argslen == 0) {
                m = c.getDeclaredMethod(methodName);
            } else {
                Method[] methodes = c.getDeclaredMethods();
                for (Method method : methodes) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (method.getName().equals(methodName) && parameterTypes.length == argslen) {
                        int i;
                        for (i = 0; i < argslen; i++) {
                            if (argsTypes[i] != parameterTypes[i]) {
                                break;
                            }
                        }
                        if (i == argslen) {
                            m = method;
                            break;
                        }
                    }
                }
            }
            if (m == null) {
                String argsName = Arrays.toString(argsTypes).replace("[", "(").replace("]", ")");
                throw new NoSuchMethodException(methodName + argsName);
            }
            m.setAccessible(true);
            result = m.invoke(o, args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 使用反射设置变量值
     *
     * @param target    被调用对象
     * @param fieldName 被调用对象的字段，一般是成员变量或静态变量，不可是常量！
     * @param value     值
     * @param <T>       value类型，泛型
     */
    public static <T> void setValue(Object target, String fieldName, T value) {
        try {
            Class c = target.getClass();
            Field f = c.getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(target, value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用反射获取变量值
     *
     * @param target    被调用对象
     * @param fieldName 被调用对象的字段，一般是成员变量或静态变量，不可以是常量
     * @param <T>       返回类型，泛型
     * @return 值
     */
    public static <T> T getValue(Object target, String fieldName) {
        T value = null;
        Class c = target.getClass();
        try {
            Field f = c.getDeclaredField(fieldName);
            f.setAccessible(true);
            value = (T) f.get(target);
        } catch (NoSuchFieldException e) {
            try {
                value = getSuperValue(target, c.getSuperclass(), fieldName);
            } catch (Exception ee) {
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static <T> T getSuperValue(Object target, Class superClass, String fieldName) {
        T value = null;
        try {
            Field f = superClass.getDeclaredField(fieldName);
            f.setAccessible(true);
            value = (T) f.get(target);
        } catch (NoSuchFieldException e) {
            if (target != Object.class) {
                value = getSuperValue(target, superClass.getSuperclass(), fieldName);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return value;
    }


    public static List<Class> getAllClassByInterface(Class c) {
        // 返回结果
        List<Class> returnClassList = new ArrayList<Class>();
        // 如果不是一个接口，则不做处理
        if (c.isInterface()) {
            // 获得当前的包名
            String packageName = c.getPackage().getName();
            try {
                // 获得当前包下以及子包下的所有类
                List<Class> allClass = getClasses(packageName);
                // 判断是否是同一个接口
                for (int i = 0; i < allClass.size(); i++) {
                    // 判断是不是一个接口
                    if (c.isAssignableFrom(allClass.get(i))) {
                        // 本身不加进去
                        if (!c.equals(allClass.get(i))) {
                            returnClassList.add(allClass.get(i));
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return returnClassList;
    }

    // 从一个包中查找出所有的类，在jar包中不能查找
    private static List<Class> getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread()
                .getContextClassLoader();
        // 用'/'代替'.'路径
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    private static List<Class> findClasses(File directory, String packageName)
            throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file,
                        packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                // 去掉'.class'
                classes.add(Class.forName(packageName
                        + '.'
                        + file.getName().substring(0,
                        file.getName().length() - 6)));

            }
        }
        return classes;
    }


    /**
     * 获取类第index个泛型类型
     *
     * @param target
     * @param index
     * @param <T>
     * @return
     */
    public static <T> Class<T> getDefinedTClass(Object target, int index) {
        Class<T> tClass = null;
        Type type = target.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            // 强制转化“参数化类型”
            ParameterizedType parameterizedType = (ParameterizedType) type;
            // 参数化类型中可能有多个泛型参数
            Type[] types = parameterizedType.getActualTypeArguments();
            // 获取数据的第一个元素(User.class)
            tClass = (Class<T>) types[index];
        }

//            Class<T> tClass = (Class<T>) ((ParameterizedType) target.getClass().getGenericSuperclass()).getActualTypeArguments()[index];
        return tClass;
    }

    public static <T> T getDefinedTInstance(Object target, int index) {
        return getDefinedTInstance(target, index, new Class[]{}, new Object[]{});
    }

    public static <T> T getDefinedTInstance(Object target, int index, Class[] consParam, Object[] param) {
        Class<T> tClass = getDefinedTClass(target, index);
        if (tClass != null) {
            try {
                return tClass.getConstructor(consParam).newInstance(param);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    return tClass.newInstance();
                } catch (Exception instantiationException) {
                    instantiationException.printStackTrace();
                }
            }
        }
        return null;
    }
}
