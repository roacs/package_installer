import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackageInstaller
{
    // TODO
    // - make the dependency resolution smart - look in a list of installed areas (in priority) and return packages found and let the user choose?
    // - test with multiple tiered installations (like an installer calling other installers)
    // - make the prompt output better
    // - how to handle shortcuts? 'rt' desktop folder divided by shortcuts? release generate the shortcuts?
    private static final Path PACKAGE_PATH = Paths.get("package");
    private static final Path PATHS_FILE   = Paths.get("paths");

    public static void main(String[] args) throws IOException
    {
        List<String> argumentList = new ArrayList<>();
        List<String> flagList = new ArrayList<>();
        for (int i = 0; i < args.length; i++)
        {
            if (args[i].startsWith("-"))
            {
                flagList.add(args[i].substring(1));
            }
            else
            {
                argumentList.add(args[i]);
            }
        }
        
        if (argumentList.size() != 4)
        {
            printUsageAndExit();
        }
        
        String version = argumentList.get(0);
        String packageName = argumentList.get(1);
        List<String> dependencies = Arrays.asList(argumentList.get(2).split("\\s+"));
        Path defaultPath = Paths.get(argumentList.get(3));
        
        System.out.print("Installing '" + packageName + "'...");
        Path installPath;
        Map<String, String> dependencyPaths = new HashMap<>();
        if (flagList.contains("q"))
        {
            installPath = defaultPath.resolve(packageName);
            dependencies.forEach((d) -> dependencyPaths.put(d, defaultPath.resolve(d).toString()));
        }
        else
        {
            System.out.println();
            System.out.println("Configure the destination to which this package will be installed.");
            installPath = readPath(defaultPath.resolve(packageName));
            
            System.out.println();
            System.out.println("Enter the paths to the dependencies for this package.");
            
            for (String dependency : dependencies)
            {
                System.out.println();
                System.out.println(dependency);
                dependencyPaths.put(dependency, readPath(defaultPath.resolve(dependency)).toString());
            }
        }
        
        deleteDirectory(installPath);
        copyDirectory(PACKAGE_PATH.resolve(version), installPath);
        if (dependencyPaths.size() > 0)
        {
            Files.write(installPath.resolve(PATHS_FILE), dependencyPaths.values());
        }
        
        System.out.println("Done");
    }
    
    private static Path readPath(Path defaultPath)
    {
        System.out.println("The default path is: " + defaultPath);
        System.out.print("Enter new path (or blank to use default):");
        String input = System.console().readLine();
        return input.isEmpty() ? defaultPath : Paths.get(input);
    }
    
    private static void printUsageAndExit()
    {
        System.out.println("TODO");
    }
    
    private static void deleteDirectory(final Path directory) throws IOException
    {
        if (Files.exists(directory))
        {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>()
            {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
                {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }
    
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
                {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }
    
    private static void copyDirectory(final Path source, final Path target) throws IOException
    {
        Files.walkFileTree(source, new SimpleFileVisitor<Path>()
        {
            @Override
            public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException
            {
                Files.createDirectories(target.resolve(source.relativize(dir)));
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException
            {
                Files.copy(file, target.resolve(source.relativize(file)));
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
