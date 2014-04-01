package org.forgeide.filesystem;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.WatchService;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.Set;

/**
 * 
 * @author Shane Bryzak
 *
 */
public class ResourceFileSystem extends FileSystem
{
   public static final String SEPARATOR = "/";

   private ResourceFileSystemProvider provider;

   public ResourceFileSystem(ResourceFileSystemProvider provider) 
   {
      this.provider = provider;
   }

   @Override
   public void close() throws IOException
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public Iterable<FileStore> getFileStores()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Path getPath(String first, String... more)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public PathMatcher getPathMatcher(String syntaxAndPattern)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Iterable<Path> getRootDirectories()
   {
      
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public String getSeparator()
   {
      return SEPARATOR;
   }

   @Override
   public UserPrincipalLookupService getUserPrincipalLookupService()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public boolean isOpen()
   {
      // For now we'll assume the FileSystem is always open
      return true;
   }

   @Override
   public boolean isReadOnly()
   {
      return false;
   }

   @Override
   public WatchService newWatchService() throws IOException
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public FileSystemProvider provider()
   {
      return provider;
   }

   @Override
   public Set<String> supportedFileAttributeViews()
   {
      // TODO Auto-generated method stub
      return null;
   }

}
