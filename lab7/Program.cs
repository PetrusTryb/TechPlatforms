namespace MyProject;
class Program
{
    static void Main( string[] args ) {
        var path = args[0];
        //Console.WriteLine( $"Path: {path}" );
        var directory = new DirectoryInfo( path );
        PrintPathContents( directory );
        var oldestElement = directory.FindOldestElement();
        Console.WriteLine( $"Oldest element: {oldestElement.FullName} {oldestElement.LastWriteTime}" );

        var allElements = directory.EnumerateFileSystemInfos()
            .Select(x => new { Name = x.Name, SizeOrCount = x.GetType() == typeof(DirectoryInfo) ? ((DirectoryInfo)x).EnumerateFileSystemInfos().Count() : ((FileInfo)x).Length })
            .OrderBy( x => x.Name.Length )
            .ThenBy( x => x.Name )
            .ToDictionary( x => x.Name, x => x.SizeOrCount );
    }

    static void PrintPathContents( DirectoryInfo directory, int depth = 0 ) {
        foreach (var subDirectory in directory.GetDirectories())
        {
            Console.WriteLine($"{new string(' ', depth * 4)}{subDirectory.Name} {subDirectory.EnumerateFileSystemInfos().Count()} {subDirectory.GetDOSAttributes()}");
            PrintPathContents(subDirectory, depth + 1);
        }

        foreach ( var file in directory.GetFiles() )
        {
            Console.WriteLine( $"{new string( ' ', depth * 4)}{file.Name} {file.Length}B {file.GetDOSAttributes()}" );
        }
    }
}