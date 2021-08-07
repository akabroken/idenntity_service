

/*---------------------------------------------------------------------------*\
 *																			 
 * InterSwitch Ltd.															 
 * Kenya																	 
 * 			
 *
 * File Name:		suppliers.sql												  
 * Version Number:	1												 
 *
 * 
 *
 * Copyright (C) 2021                              							 
 * This software may not be copied or distributed in any form without the	 
 * written permission of InterSwitch Ltd.									 
 *																			 
\*---------------------------------------------------------------------------*/

/*=========================*/
/* 	MODIFICATION LOG 	   */
/*=========================*/
/*
 *	Ver.	Author			Date			Doc. Ref.	Summary of Changes
 *  1		Linus			07/08/2021      			Created
 *
 *
 *	Last modified in: v1.0.0
\*---------------------------------------------------------------------------*/

USE tims
GO

--------------------------------------------------------------------------------
PRINT ''
PRINT 'Creating Table: suppliers'
PRINT ''
--------------------------------------------------------------------------------
GO

IF EXISTS (SELECT 1 FROM dbo.sysobjects WHERE id = object_id(N'dbo.suppliers')
AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
PRINT 'TABLE suppliers ALREADY EXISTS... CREATE TABLE ABORTED'
ELSE
CREATE TABLE [dbo].[suppliers](
	[serno] [bigint] NOT NULL PRIMARY KEY,
	[name] [varchar](50) NOT NULL,	
	[location] [varchar](100) NOT NULL,
	[coordinates] [varchar](100) NULL,			
	[pin] [varchar](50) NOT NULL,				
	[createdon] [datetime] DEFAULT GETDATE() NOT NULL,
	[status] [varchar](10) DEFAULT 'A' NOT NULL
)
-------------------------------------------------------------------------------------------
IF EXISTS (SELECT 1 FROM  sys.indexes WHERE object_id  = OBJECT_ID('suppliers') AND name = 'idx_suppliers_01')
PRINT 'CONSTRAINT idx_suppliers_01 ALREADY EXISTS...ALTER TABLE ABORTED'
ELSE
CREATE UNIQUE NONCLUSTERED INDEX [idx_suppliers_01] ON [dbo].[suppliers]
(
	[pin] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
-------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------
PRINT 'Created: suppliers'
-------------------------------------------------------------------------------------------
GRANT SELECT, INSERT, DELETE, UPDATE ON suppliers TO tims_private
GO
-------------------------------------------------------------------------------------------
IF EXISTS (SELECT 1 FROM  sys.sequences WHERE name = 'suppliers_serno_seq')
    PRINT 'SEQUENCE tims.suppliers_serno_seq ALREADY EXISTS...'
ELSE
CREATE SEQUENCE tims.suppliers_serno_seq  
    START WITH 1  
    INCREMENT BY 1 ;  
GO 
PRINT 'Created: suppliers_serno_seq SEQUENCE'
-------------------------------------------------------------------------------------------

PRINT 'Creating: trg_users_INSERT trigger'
DROP TRIGGER [dbo].[trg_suppliers_INSERT] 
GO
CREATE  TRIGGER [dbo].[trg_suppliers_INSERT] 
   ON  [dbo].[suppliers]
   AFTER INSERT
AS 
BEGIN
SET NOCOUNT ON 
         --Insert audit info
      declare @rowserno bigint;
	  declare @tab varchar(30) = 'suppliers';
	  declare @log varchar(30) = 'Inserted';
      
	  select @rowserno=serno from inserted;
	  	EXEC [dbo].[psp_insert_applog] @log,@tab,'rowserno',@rowserno,'',@rowserno	
	  	
 END
GO
PRINT 'Created: trg_suppliers_INSERT trigger'
-----------------------------------------------------------------------------------------------
PRINT 'Creating: trg_suppliers_UPDATE trigger'
DROP TRIGGER [dbo].[trg_suppliers_UPDATE] 
GO
CREATE  TRIGGER [dbo].[trg_suppliers_UPDATE] 
   ON  [dbo].[suppliers]
   AFTER UPDATE
AS 
BEGIN
SET NOCOUNT ON 
         --Update audit info
      declare @rowserno bigint;
	  declare @tab varchar(30) = 'suppliers';
	  declare @log varchar(30) = 'Updated';
	  declare @oldvalue varchar(300);
	  declare @newvalue varchar(300);

	  select @rowserno=serno from deleted;  

      IF EXISTS(SELECT 1  FROM deleted d inner join inserted i on i.serno=d.serno and d.name<>i.name) 
      	BEGIN
      		SELECT @oldvalue = d.name,@newvalue =i.name  FROM deleted d inner join inserted i on i.serno=d.serno and d.name<>i.name
      		EXEC [dbo].[psp_insert_applog] @log,@tab,'name',@rowserno,@oldvalue,@newvalue	
      	END

      IF EXISTS(SELECT 1  FROM deleted d inner join inserted i on i.serno=d.serno and d.location<>i.location) 
      	BEGIN
      		SELECT @oldvalue = d.location,@newvalue =i.location  FROM deleted d inner join inserted i on i.serno=d.serno and d.location<>i.location
      		EXEC [dbo].[psp_insert_applog] @log,@tab,'location',@rowserno,@oldvalue,@newvalue	
      	END

      IF EXISTS(SELECT 1  FROM deleted d inner join inserted i on i.serno=d.serno and d.coordinates<>i.coordinates) 
      	BEGIN
      		SELECT @oldvalue = d.coordinates,@newvalue =i.coordinates  FROM deleted d inner join inserted i on i.serno=d.serno and d.coordinates<>i.coordinates
      		EXEC [dbo].[psp_insert_applog] @log,@tab,'coordinates',@rowserno,@oldvalue,@newvalue	
      	END

      IF EXISTS(SELECT 1  FROM deleted d inner join inserted i on i.serno=d.serno and d.status<>i.status) 
      	BEGIN
      		SELECT @oldvalue = d.status,@newvalue =i.status  FROM deleted d inner join inserted i on i.serno=d.serno and d.status<>i.status
      		EXEC [dbo].[psp_insert_applog] @log,@tab,'status',@rowserno,@oldvalue,@newvalue	
      	END

END
GO
PRINT 'Created: trg_suppliers_UPDATE trigger'

---------------------------------------------------------------------------------------------------------------------------------------------
PRINT 'Creating: trg_suppliers_DELETE trigger'
DROP TRIGGER [dbo].[trg_suppliers_DELETE] 
GO
CREATE  TRIGGER [dbo].[trg_suppliers_DELETE] 
   ON  [dbo].[suppliers]
   AFTER DELETE
AS 
BEGIN
SET NOCOUNT ON 
      --Delete audit info

      declare @rowserno bigint;
	  declare @tab varchar(30) = 'suppliers';
	  declare @log varchar(30) = 'Deleted';
      
	  select @rowserno=serno from deleted;
	  	EXEC [dbo].[psp_insert_applog] @log,@tab,'rowserno',@rowserno,@rowserno,''	  	
 END
GO
PRINT 'Created: trg_suppliers_DELETE trigger'
--------------------------------------------------------------------------------------------------------------------------------------------

