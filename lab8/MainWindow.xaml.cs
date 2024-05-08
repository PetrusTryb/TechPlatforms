using System.IO;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Windows.Forms;

namespace Lab8
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow( ) {
            InitializeComponent();
        }

        private void MenuItemOpen_Click( object sender, RoutedEventArgs e ) {
            var folderDialog = new FolderBrowserDialog(){ Description = "Select directory to open" };
            if ( folderDialog.ShowDialog() == System.Windows.Forms.DialogResult.OK )
            {
                TreeView1.Items.Clear();
                var rootItem = new TreeViewItem() { Header = folderDialog.SelectedPath, Tag = folderDialog.SelectedPath };
                TreeView1.Items.Add( rootItem );
                EnumerateDirectory( folderDialog.SelectedPath, rootItem );
            }
        }

        private void EnumerateDirectory( string path, TreeViewItem item ) {
            var files = Directory.GetFiles( path );
            var directories = Directory.GetDirectories( path );
            foreach ( var file in files )
            {
                var fileItem = new TreeViewItem() { Header = file.Split("\\").Last(), Tag = file };
                item.Items.Add( fileItem );
                CreateContextMenu( true, fileItem );
            }
            foreach ( var directory in directories )
            {
                var directoryItem = new TreeViewItem() { Header = directory.Split("\\").Last(), Tag = directory };
                item.Items.Add( directoryItem );
                EnumerateDirectory( directory, directoryItem );
                CreateContextMenu( false, directoryItem );
            }
        }

        private void CreateContextMenu( bool isFile, TreeViewItem item ) {
            var contextMenu = new ContextMenu();
            var menuItemDelete = new MenuItem() { Header = "Delete" };
            menuItemDelete.Click += ( sender, e ) =>
            {
                if ( isFile )
                {
                    File.Delete(item.Tag.ToString()!);
                }
                else
                {
                    Directory.Delete(item.Tag.ToString()!, true);
                }
                item.Items.Clear();
                TreeViewItem parent = (TreeViewItem)item.Parent;
                parent.Items.Remove( item );
            };
            contextMenu.Items.Add( menuItemDelete );

            if( isFile )
            {
                var menuItemOpen = new MenuItem() { Header = "Open" };
                menuItemOpen.Click += ( sender, e ) =>
                {
                    var content = File.ReadAllText( item.Tag.ToString()! );
                    FileContents.Text = content;
                    
                };
                contextMenu.Items.Add( menuItemOpen );
            }

            item.ContextMenu = contextMenu;
        }

        private void MenuItemExit_Click( object sender, RoutedEventArgs e ) {
            Close();
        }
    }
}