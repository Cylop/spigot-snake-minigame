package at.malibu.general.module;

import at.malibu.api.internals.handler.Handler;
import at.malibu.api.module.Module;
import at.malibu.api.module.ModuleInfo;
import at.malibu.general.exception.GeneralException;
import at.malibu.general.internals.timing.Timing;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import lombok.NonNull;
import lombok.extern.apachecommons.CommonsLog;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The module handler takes care of all modules.
 */
@Singleton
@CommonsLog
public final class ModuleHandler implements Handler {

    private static Map<Class<Module>, Module> offeredModules = new HashMap<>();
    private static boolean isAcceptingOffers = true;

    @NonNull
    private List<Module> modules = new ArrayList<>();

    @Inject
    private Injector injector;

    @NonNull
    public static Map<Class<Module>, Module> getOfferedModules() {
        return offeredModules;
    }

    /**
     * Adds a module to this handler, will be injected and then added to the loading queue
     *
     * @param module the module to add
     */
    public static void offerModule(@NonNull Module module) {
        if (isAcceptingOffers) {
            //noinspection unchecked
            offeredModules.put((Class<Module>) module.getClass(), module);
        } else {
            throw new GeneralException("Module offers closed! Make sure you offer the module onLoad!");
        }
    }

    @Override
    public void enable() {
        log.info("Loading modules");
        findModules();

        modules.forEach(Module::enable);
    }

    @Override
    public void disable() {
        modules.forEach(Module::disable);
        modules.clear();

    }

    private void findModules() {
        try (final Timing timing = new Timing("RegisterModules")) {
            isAcceptingOffers = false;
            for (Class<? extends Module> clazz : offeredModules.keySet()) {
                ModuleInfo info = clazz.getAnnotation(ModuleInfo.class);
                if (info == null) {
                    log.warn("Class " + clazz.getSimpleName() + " has no module info!");
                    continue;
                }
                log.info("Loading module " + info.name() + " v" + info.version() + " by " + Arrays
                        .toString(info.authors()));
                if (Module.class.isAssignableFrom(clazz)) {
                    Module module = offeredModules.get(clazz);
                    this.injector.injectMembers(module);
                    this.modules.add(module);
                } else {
                    log.warn("Class " + clazz.getSimpleName() + " has the ModuleInfo annotation but does not implement Module!");
                }
            }

            offeredModules.clear();
            log.info("Loaded " + this.modules.size() + " modules!");
        }
    }

    public List<ClassLoader> getModuleClassLoaders() {
        if (offeredModules.size() != 0) {
            // we haven't enabled the module handler yet, grab raw modules
            return offeredModules.keySet().stream().map(Class::getClassLoader).collect(Collectors.toList());
        }
        return modules.stream().map(module -> module.getClass().getClassLoader()).collect(Collectors.toList());
    }
}