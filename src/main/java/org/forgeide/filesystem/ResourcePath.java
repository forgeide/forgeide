package org.forgeide.filesystem;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Iterator;

/**
 *
 * @author Shane Bryzak
 *
 */
public class ResourcePath implements Path
{
   private FileSystem fileSystem;

   public ResourcePath(FileSystem fileSystem) {
      this.fileSystem = fileSystem;
   }

   @Override
   public int compareTo(Path other)
   {
      // TODO Auto-generated method stub
      return 0;
   }

   @Override
   public boolean endsWith(Path other)
   {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public boolean endsWith(String other)
   {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public Path getFileName()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public FileSystem getFileSystem()
   {
      return fileSystem;
   }

   @Override
   public Path getName(int index)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public int getNameCount()
   {
      // TODO Auto-generated method stub
      return 0;
   }

   @Override
   public Path getParent()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Path getRoot()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public boolean isAbsolute()
   {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public Iterator<Path> iterator()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Path normalize()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public WatchKey register(WatchService watcher, Kind<?>... events) throws IOException
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public WatchKey register(WatchService watcher, Kind<?>[] events, Modifier... modifiers) throws IOException
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Path relativize(Path other)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Path resolve(Path other)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Path resolve(String other)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Path resolveSibling(Path other)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Path resolveSibling(String other)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public boolean startsWith(Path other)
   {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public boolean startsWith(String other)
   {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public Path subpath(int beginIndex, int endIndex)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Path toAbsolutePath()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public File toFile()
   {
      return new File(toUri());
   }

   @Override
   public Path toRealPath(LinkOption... options) throws IOException
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public URI toUri()
   {
      // TODO Auto-generated method stub
      return null;
   }

}
