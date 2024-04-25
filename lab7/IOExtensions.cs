using System.Text;

namespace MyProject
{
    internal static class IOExtensions
    {

        public static FileInfo? FindOldestElement( this DirectoryInfo directoryInfo ) {
            var directiories = directoryInfo.GetDirectories();
            var files = directoryInfo.GetFiles().OrderBy(f => f.CreationTime );
            var oldestElement = files.FirstOrDefault();
            foreach (DirectoryInfo subDirectory in directiories)
            {
                var oldestFileInSubDirectory = subDirectory.FindOldestElement();
                if (oldestElement == null || oldestFileInSubDirectory?.CreationTime < oldestElement.CreationTime)
                {
                    oldestElement = oldestFileInSubDirectory;
                }
            }
            return oldestElement;
        }

        public static string GetDOSAttributes( this FileSystemInfo fileSystemInfo ) {
            var attributes = new StringBuilder();
            attributes.Append((fileSystemInfo.Attributes & FileAttributes.ReadOnly) == FileAttributes.ReadOnly ? "R" : "-");
            attributes.Append( (fileSystemInfo.Attributes & FileAttributes.Archive ) == FileAttributes.Archive ? "A" : "-" );
            attributes.Append((fileSystemInfo.Attributes & FileAttributes.Hidden) == FileAttributes.Hidden ? "H" : "-");
            attributes.Append( (fileSystemInfo.Attributes & FileAttributes.System ) == FileAttributes.System ? "S" : "-" );
            
            return attributes.ToString();
        }
    }
}