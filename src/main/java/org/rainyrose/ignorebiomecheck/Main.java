package org.rainyrose.ignorebiomecheck;

import cn.nukkit.plugin.PluginBase;
import org.rainyrose.ignorebiomecheck.dump.Data3dSerializerDump;

import java.lang.ref.WeakReference;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * @author Rainy_Rose
 */
public class Main extends PluginBase {

    @Override
    public void onLoad() {
        try {
            // 把Data3dSerializer类替换成没有群系检查的
            byte[] dump = Data3dSerializerDump.dump();
            loadClass(this.getServer().getPrimaryThread().getContextClassLoader(), "cn.nukkit.level.format.leveldb.serializer.Data3dSerializer", dump);
            this.getLogger().info("Data3dSerializer类修改成功");
        } catch (Exception e) {
            this.getLogger().error("Data3dSerializer类修改失败", e);
        }
    }

    private static Class<?> loadClass(ClassLoader loader, String className, byte[] b) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InaccessibleObjectException {
        Class<?> clazz;
        Class<?> cls = Class.forName("java.lang.ClassLoader");
        java.lang.reflect.Method method = cls.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
        Objects.requireNonNull(method).setAccessible(true);
        try {
            var args = new Object[]{className, b, 0, b.length};
            clazz = (Class<?>) method.invoke(loader, args);
        } finally {
            method.setAccessible(false);
        }
        return clazz;
    }
}
