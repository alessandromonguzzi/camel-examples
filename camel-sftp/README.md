# Connect to SFTP server and download files
This example connects to the SFTP server on port 2222 using user=foo and password=pass every 5 seconds (see scheduler configuration) and "downloads" the file. Since the "to" route is mock the file is simply removed from ftp server.
Note the disconnect=true and delete=true properties.

This example uses camel-blueprint, camel-sftp and camel-quartz2 features.

## Setup

1. Install and run SFTP server from docker:
```
sudo podman run --name sftp -p 2222:22 atmoz/sftp foo:pass:::upload
```
See [documentation]|(https://hub.docker.com/r/atmoz/sftp/) for details.

2. Put test files inside the upload directory in the ftp server:
```
sftp -P 2222 foo@127.0.0.1 
sftp> cd upload/
sftp> put myfile.csv
```

3. Start Fuse and install features:

```
./bin/fuse
feature:install camel-blueprint camel-ftp camel-quartz2
```

4. Install blueprint.xml and then check logs for the route to run:
```
cp blueprint.xml <fuse_deploy_dir>
```

