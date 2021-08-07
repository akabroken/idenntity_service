/*---------------------------------------------------------------------------*\
 *																			 
 * InterSwitch Ltd.															 
 * Kenya																	 
 * 			
 *
 * File Name:		create-tims-db.sql												  
 * Version Number:	1												 									 
 *																			 
\*---------------------------------------------------------------------------*/
USE master
GO

IF exists (SELECT name FROM master.dbo.sysdatabases WHERE name = 'tims')
BEGIN
	PRINT ''
	PRINT 'The tims database already exists. No database was created.'
END
ELSE
CREATE DATABASE tims --ON PRIMARY
--(
--	NAME = N'tims_db',
--	FILENAME = N'E:\MSSQL\Data\tims_db.mdf',
--  	SIZE = 3072KB,
--	FILEGROWTH = 1024KB
--)
--LOG ON
--(
--	NAME = N'tims_log',
--	FILENAME = 'C:\Program Files\Microsoft SQL Server\MSSQL12.MSSQLSERVER\MSSQL\DATA\tims_log.ldf',
--  	SIZE = 1024KB,
--	FILEGROWTH = 10%
--)
GO

PRINT '-----------------------------------------------------'
PRINT ' Adding user logins'
PRINT '-----------------------------------------------------'
PRINT ''
GO

USE master
GO

IF NOT EXISTS (SELECT * FROM syslogins WHERE loginname = 'tims')
BEGIN
	EXEC sp_addlogin 
		@loginame='tims', 
		@passwd='%_tism*B548f6', 
		@defdb='tims'
END
GO


PRINT '-----------------------------------------------------'
PRINT ' Adding user roles'
PRINT '-----------------------------------------------------'
PRINT ''
GO

USE tims
GO

IF NOT EXISTS (SELECT * FROM sysusers WHERE (name = 'tims_private') AND (issqlrole = 1))
BEGIN
	EXEC sp_addrole 'tims_private'
	EXEC sp_adduser  
		@loginame='tims', 
		@name_in_db='tims', 
		@grpname='tims_private'
END
GO
