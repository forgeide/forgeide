package org.forgeide.filesystem;

import java.io.IOException;
import java.net.URI;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Shane Bryzak
 *
 */
public class ResourceFileSystemProvider extends FileSystemProvider
{
   public static final String SCHEME = "forgeide";
   

   @Override
   public void checkAccess(Path arg0, AccessMode... arg1) throws IOException
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void copy(Path arg0, Path arg1, CopyOption... arg2) throws IOException
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void createDirectory(Path arg0, FileAttribute<?>... arg1) throws IOException
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void delete(Path arg0) throws IOException
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public <V extends FileAttributeView> V getFileAttributeView(Path arg0, Class<V> arg1, LinkOption... arg2)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public FileStore getFileStore(Path arg0) throws IOException
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public FileSystem getFileSystem(URI arg0)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Path getPath(URI arg0)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public String getScheme()
   {
      return SCHEME;
   }

   @Override
   public boolean isHidden(Path arg0) throws IOException
   {
      return false;
   }

   @Override
   public boolean isSameFile(Path arg0, Path arg1) throws IOException
   {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public void move(Path arg0, Path arg1, CopyOption... arg2) throws IOException
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public SeekableByteChannel newByteChannel(Path arg0, Set<? extends OpenOption> arg1, FileAttribute<?>... arg2)
            throws IOException
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public DirectoryStream<Path> newDirectoryStream(Path arg0, Filter<? super Path> arg1) throws IOException
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public FileSystem newFileSystem(URI arg0, Map<String, ?> arg1) throws IOException
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public <A extends BasicFileAttributes> A readAttributes(Path arg0, Class<A> arg1, LinkOption... arg2)
            throws IOException
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Map<String, Object> readAttributes(Path arg0, String arg1, LinkOption... arg2) throws IOException
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public void setAttribute(Path arg0, String arg1, Object arg2, LinkOption... arg3) throws IOException
   {
      // TODO Auto-generated method stub
      
   }

}
