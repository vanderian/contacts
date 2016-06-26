package sk.vander.contacts.data;

import dagger.Module;
import sk.vander.contacts.data.api.DebugApiModule;

/**
 * Created by arashid on 27/06/16.
 */
@Module(includes = {DataModule.class, DebugApiModule.class})
public class DebugDataModule {
}
