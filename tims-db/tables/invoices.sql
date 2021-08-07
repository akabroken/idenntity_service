

/*---------------------------------------------------------------------------*\
 *																			 
 * InterSwitch Ltd.															 
 * Kenya																	 
 * 			
 *
 * File Name:		invoices.sql												  
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
PRINT 'Creating Table: invoices'
PRINT ''
--------------------------------------------------------------------------------
GO

IF EXISTS (SELECT 1 FROM dbo.sysobjects WHERE id = object_id(N'dbo.invoices')
AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
PRINT 'TABLE invoices ALREADY EXISTS... CREATE TABLE ABORTED'
ELSE
CREATE TABLE [dbo].[invoices](
	[serno] [bigint] NOT NULL PRIMARY KEY,
	[suppserno] [bigint] NOT NULL FOREIGN KEY REFERENCES SUPPLIERS(SERNO),
	[tradserno] [bigint] NOT NULL FOREIGN KEY REFERENCES invoices(SERNO),
	[type] [varchar](10) NOT NULL CHECK(type IN ('VIRTUAL','POS')),			
	[msn] [varchar](50) NOT NULL,	
	[msntype] [varchar](1) NOT NULL CHECK(msntype IN ('A','B','C','D')),
	[lastinvoicenumber] [varchar](100) NULL,
	[nextinvoicenumber] [varchar](100) NULL,			
	[name] [varchar](100) NOT NULL,	
	[location] [varchar](100) NULL,
	[coordinates] [varchar](100) NULL,
	[rsapubkey] [varchar](3000) NULL,	
	[status] [varchar](10) DEFAULT 'NORM' NOT NULL,
	[createdon] [datetime] DEFAULT GETDATE() NOT NULL	
)
-------------------------------------------------------------------------------------------
IF EXISTS (SELECT 1 FROM  sys.indexes WHERE object_id  = OBJECT_ID('invoices') AND name = 'idx_invoices_01')
PRINT 'CONSTRAINT idx_invoices_01 ALREADY EXISTS...ALTER TABLE ABORTED'
ELSE
CREATE UNIQUE NONCLUSTERED INDEX [idx_invoices_01] ON [dbo].[invoices]
(
	[msn] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
-------------------------------------------------------------------------------------------
-------------------------------------------------------------------------------------------
PRINT 'Created: invoices'
-------------------------------------------------------------------------------------------
GRANT SELECT, INSERT, DELETE, UPDATE ON invoices TO tims_private
GO
-------------------------------------------------------------------------------------------
IF EXISTS (SELECT 1 FROM  sys.sequences WHERE name = 'invoices_serno_seq')
    PRINT 'SEQUENCE tims.invoices_serno_seq ALREADY EXISTS...'
ELSE
CREATE SEQUENCE tims.invoices_serno_seq  
    START WITH 1  
    INCREMENT BY 1 ;  
GO 
PRINT 'Created: invoices_serno_seq SEQUENCE'
-------------------------------------------------------------------------------------------

PRINT 'Creating: trg_invoices_INSERT trigger'
DROP TRIGGER [dbo].[trg_invoices_INSERT] 
GO
CREATE  TRIGGER [dbo].[trg_invoices_INSERT] 
   ON  [dbo].[invoices]
   AFTER INSERT
AS 
BEGIN
SET NOCOUNT ON 
         --Insert audit info
      declare @rowserno bigint;
	  declare @tab varchar(30) = 'invoices';
	  declare @log varchar(30) = 'Inserted';
      
	  select @rowserno=serno from inserted;
	  	EXEC [dbo].[psp_insert_applog] @log,@tab,'rowserno',@rowserno,'',@rowserno	
	  	
 END
GO
PRINT 'Created: trg_invoices_INSERT trigger'
-----------------------------------------------------------------------------------------------
PRINT 'Creating: trg_invoices_UPDATE trigger'
DROP TRIGGER [dbo].[trg_invoices_UPDATE] 
GO
CREATE  TRIGGER [dbo].[trg_invoices_UPDATE] 
   ON  [dbo].[invoices]
   AFTER UPDATE
AS 
BEGIN
SET NOCOUNT ON 
         --Update audit info
      declare @rowserno bigint;
	  declare @tab varchar(30) = 'invoices';
	  declare @log varchar(30) = 'Updated';
	  declare @oldvalue varchar(3000);
	  declare @newvalue varchar(3000);

	  select @rowserno=serno from deleted;  

      IF EXISTS(SELECT 1  FROM deleted d inner join inserted i on i.serno=d.serno and d.name<>i.name) 
      	BEGIN
      		SELECT @oldvalue = d.name,@newvalue =i.status  FROM deleted d inner join inserted i on i.serno=d.serno and d.name<>i.name
      		EXEC [dbo].[psp_insert_applog] @log,@tab,'name',@rowserno,@oldvalue,@newvalue	
      	END

      IF EXISTS(SELECT 1  FROM deleted d inner join inserted i on i.serno=d.serno and d.type<>i.type) 
      	BEGIN
      		SELECT @oldvalue = d.type,@newvalue =i.type  FROM deleted d inner join inserted i on i.serno=d.serno and d.type<>i.type
      		EXEC [dbo].[psp_insert_applog] @log,@tab,'type',@rowserno,@oldvalue,@newvalue	
      	END

      IF EXISTS(SELECT 1  FROM deleted d inner join inserted i on i.serno=d.serno and d.msntype<>i.msntype) 
      	BEGIN
      		SELECT @oldvalue = d.msntype,@newvalue =i.msntype  FROM deleted d inner join inserted i on i.serno=d.serno and d.msntype<>i.msntype
      		EXEC [dbo].[psp_insert_applog] @log,@tab,'msntype',@rowserno,@oldvalue,@newvalue	
      	END

      IF EXISTS(SELECT 1  FROM deleted d inner join inserted i on i.serno=d.serno and d.msn<>i.msn) 
      	BEGIN
      		SELECT @oldvalue = d.msn,@newvalue =i.msn  FROM deleted d inner join inserted i on i.serno=d.serno and d.msn<>i.msn
      		EXEC [dbo].[psp_insert_applog] @log,@tab,'msn',@rowserno,@oldvalue,@newvalue	
      	END

      IF EXISTS(SELECT 1  FROM deleted d inner join inserted i on i.serno=d.serno and d.coordinates<>i.coordinates) 
      	BEGIN
      		SELECT @oldvalue = d.coordinates,@newvalue =i.coordinates  FROM deleted d inner join inserted i on i.serno=d.serno and d.coordinates<>i.coordinates
      		EXEC [dbo].[psp_insert_applog] @log,@tab,'coordinates',@rowserno,@oldvalue,@newvalue	
      	END

      IF EXISTS(SELECT 1  FROM deleted d inner join inserted i on i.serno=d.serno and d.location<>i.location) 
      	BEGIN
      		SELECT @oldvalue = d.location,@newvalue =i.location  FROM deleted d inner join inserted i on i.serno=d.serno and d.location<>i.location
      		EXEC [dbo].[psp_insert_applog] @log,@tab,'location',@rowserno,@oldvalue,@newvalue	
      	END

      IF EXISTS(SELECT 1  FROM deleted d inner join inserted i on i.serno=d.serno and d.status<>i.status) 
      	BEGIN
      		SELECT @oldvalue = d.status,@newvalue =i.status  FROM deleted d inner join inserted i on i.serno=d.serno and d.status<>i.status
      		EXEC [dbo].[psp_insert_applog] @log,@tab,'status',@rowserno,@oldvalue,@newvalue	
      	END

      IF EXISTS(SELECT 1  FROM deleted d inner join inserted i on i.serno=d.serno and d.rsapubkey<>i.rsapubkey) 
      	BEGIN
      		SELECT @oldvalue = d.rsapubkey,@newvalue =i.rsapubkey  FROM deleted d inner join inserted i on i.serno=d.serno and d.rsapubkey<>i.rsapubkey
      		EXEC [dbo].[psp_insert_applog] @log,@tab,'rsapubkey',@rowserno,@oldvalue,@newvalue	
      	END
     
		
 END
GO
PRINT 'Created: trg_invoices_UPDATE trigger'

---------------------------------------------------------------------------------------------------------------------------------------------
PRINT 'Creating: trg_invoices_DELETE trigger'
DROP TRIGGER [dbo].[trg_invoices_DELETE] 
GO
CREATE  TRIGGER [dbo].[trg_invoices_DELETE] 
   ON  [dbo].[invoices]
   AFTER DELETE
AS 
BEGIN
SET NOCOUNT ON 
      --Delete audit info
      declare @rowserno bigint;
	  declare @tab varchar(30) = 'invoices';
	  declare @log varchar(30) = 'Deleted';
      
	  select @rowserno=serno from deleted;
	  	EXEC [dbo].[psp_insert_applog] @log,@tab,'rowserno',@rowserno,@rowserno,''	  	
 END
GO
PRINT 'Created: trg_invoices_DELETE trigger'
--------------------------------------------------------------------------------------------------------------------------------------------

