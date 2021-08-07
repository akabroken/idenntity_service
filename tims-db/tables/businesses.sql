

/*---------------------------------------------------------------------------*\
 *																			 
 * InterSwitch Ltd.															 
 * Kenya																	 
 * 			
 *
 * File Name:		businesses.sql												  
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
PRINT 'Creating Table: businesses'
PRINT ''
--------------------------------------------------------------------------------
GO

IF EXISTS (SELECT 1 FROM dbo.sysobjects WHERE id = object_id(N'dbo.businesses')
AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
PRINT 'TABLE businesses ALREADY EXISTS... CREATE TABLE ABORTED'
ELSE
CREATE TABLE [dbo].[businesses](
	[serno] [bigint] NOT NULL PRIMARY KEY,
	[name] [varchar](200) NOT NULL,	
	[service] [varchar](200) NOT NULL CHECK (service IN ('Agricultural','Contracted','Transportation','Utility','Retail Outlet','Clothing','Miscellaneous','Businesses','Professional and Membership Organizations','Governament')),				
	[status] [varchar](10) DEFAULT 'NORM' NOT NULL,
	[createdon] [datetime] DEFAULT GETDATE() NOT NULL	
)

-------------------------------------------------------------------------------------------
IF EXISTS (SELECT 1 FROM  sys.indexes WHERE object_id  = OBJECT_ID('businesses') AND name = 'idx_businesses_01')
PRINT 'CONSTRAINT idx_businesses_01 ALREADY EXISTS...ALTER TABLE ABORTED'
ELSE
CREATE UNIQUE  NONCLUSTERED INDEX [idx_businesses_01] ON [dbo].[businesses]
(
	[name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
-------------------------------------------------------------------------------------------
-------------------------------------------------------------------------------------------
PRINT 'Created: businesses'
-------------------------------------------------------------------------------------------
GRANT SELECT, INSERT, DELETE, UPDATE ON businesses TO tims_private
GO
-------------------------------------------------------------------------------------------
IF EXISTS (SELECT 1 FROM  sys.sequences WHERE name = 'businesses_serno_seq')
    PRINT 'SEQUENCE tims.businesses_serno_seq ALREADY EXISTS...'
ELSE
CREATE SEQUENCE tims.businesses_serno_seq  
    START WITH 1  
    INCREMENT BY 1 ;  
GO 
PRINT 'Created: businesses_serno_seq SEQUENCE'
-------------------------------------------------------------------------------------------

PRINT 'Creating: trg_businesses_INSERT trigger'
DROP TRIGGER [dbo].[trg_businesses_INSERT] 
GO
CREATE  TRIGGER [dbo].[trg_businesses_INSERT] 
   ON  [dbo].[businesses]
   AFTER INSERT
AS 
BEGIN
SET NOCOUNT ON 
         --Insert audit info
      declare @rowserno bigint;
	  declare @tab varchar(30) = 'businesses';
	  declare @log varchar(30) = 'Inserted';
      
	  select @rowserno=serno from inserted;
	  	EXEC [dbo].[psp_insert_applog] @log,@tab,'rowserno',@rowserno,'',@rowserno	
	  	
 END
GO
PRINT 'Created: trg_businesses_INSERT trigger'
-----------------------------------------------------------------------------------------------
PRINT 'Creating: trg_businesses_UPDATE trigger'
DROP TRIGGER [dbo].[trg_businesses_UPDATE] 
GO
CREATE  TRIGGER [dbo].[trg_businesses_UPDATE] 
   ON  [dbo].[businesses]
   AFTER UPDATE
AS 
BEGIN
SET NOCOUNT ON 
         --Update audit info
      declare @rowserno bigint;
	  declare @tab varchar(30) = 'businesses';
	  declare @log varchar(30) = 'Updated';
	  declare @oldvalue varchar(500);
	  declare @newvalue varchar(500);

	  select @rowserno=serno from deleted;  

      IF EXISTS(SELECT 1  FROM deleted d inner join inserted i on i.serno=d.serno and d.name<>i.name) 
      	BEGIN
      		SELECT @oldvalue = d.name,@newvalue =i.name  FROM deleted d inner join inserted i on i.serno=d.serno and d.name<>i.name
      		EXEC [dbo].[psp_insert_applog] @log,@tab,'name',@rowserno,@oldvalue,@newvalue	
      	END

      IF EXISTS(SELECT 1  FROM deleted d inner join inserted i on i.serno=d.serno and d.service<>i.service) 
      	BEGIN
      		SELECT @oldvalue = d.service,@newvalue =i.service  FROM deleted d inner join inserted i on i.serno=d.serno and d.service<>i.service
      		EXEC [dbo].[psp_insert_applog] @log,@tab,'service',@rowserno,@oldvalue,@newvalue	
      	END

      IF EXISTS(SELECT 1  FROM deleted d inner join inserted i on i.serno=d.serno and d.status<>i.status) 
      	BEGIN
      		SELECT @oldvalue = d.status,@newvalue =i.status  FROM deleted d inner join inserted i on i.serno=d.serno and d.status<>i.status
      		EXEC [dbo].[psp_insert_applog] @log,@tab,'status',@rowserno,@oldvalue,@newvalue	
      	END

 END
GO
PRINT 'Created: trg_businesses_UPDATE trigger'

---------------------------------------------------------------------------------------------------------------------------------------------
PRINT 'Creating: trg_businesses_DELETE trigger'
DROP TRIGGER [dbo].[trg_businesses_DELETE] 
GO
CREATE  TRIGGER [dbo].[trg_businesses_DELETE] 
   ON  [dbo].[businesses]
   AFTER DELETE
AS 
BEGIN
SET NOCOUNT ON 
      --Delete audit info
      declare @rowserno bigint;
	  declare @tab varchar(30) = 'businesses';
	  declare @log varchar(30) = 'Deleted';
      
	  select @rowserno=serno from deleted;
	  	EXEC [dbo].[psp_insert_applog] @log,@tab,'rowserno',@rowserno,@rowserno,''	  	
 END
GO
PRINT 'Created: trg_businesses_DELETE trigger'
--------------------------------------------------------------------------------------------------------------------------------------------

