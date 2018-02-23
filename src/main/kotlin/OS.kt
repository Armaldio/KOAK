class OS {
    enum class OS {
        WINDOWS,
        LINUX,
        MAC,
        UNSUPPORTED
    }

    companion object {
        fun getCurrentOS(): OS {
            val os = System.getProperty("os.name").toLowerCase()
            return when {
                os.indexOf("win") >= 0 -> OS.WINDOWS
                os.indexOf("mac") >= 0 -> OS.MAC
                os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0 -> OS.LINUX
                else -> OS.UNSUPPORTED
            }
        }
    }
}