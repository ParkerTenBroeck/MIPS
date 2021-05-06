package org.parker.mips.architectures;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class ProjectInformation implements Serializable {
    public File projectDIR;
    public boolean useDefaults;
    public boolean readOnly;
    public List<File> excludedFiles;
    public List<File> includedFiles;
}
