

/*---------------------------------------------------------------------------*\
 *																			 
 * InterSwitch Ltd.															 
 * Kenya																	 
 * 			
 *
 * File Name:		people.sql												  
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
PRINT 'Creating Table: people'
PRINT ''
--------------------------------------------------------------------------------
GO

IF EXISTS (SELECT 1 FROM dbo.sysobjects WHERE id = object_id(N'dbo.people')
AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
PRINT 'TABLE people ALREADY EXISTS... CREATE TABLE ABORTED'
ELSE
CREATE TABLE [dbo].[people](
	[serno] [bigint] NOT NULL PRIMARY KEY,
	[suppserno] [bigint] NOT NULL FOREIGN KEY REFERENCES SUPPLIERS(SERNO),
	[entity] [varchar](50) NOT NULL CHECK (entity IN ('traders','terminals')),			
	[entityserno] [bigint] NOT NULL,	
	[type] [varchar](50) NOT NULL CHECK (type IN ('contact','alternative')),	
	[name] [varchar](50) NOT NULL,	
	[mobile] [varchar](100) NULL,
	[email] [varchar](100) NULL,
	[status] [varchar](10) DEFAULT 'NORM' NOT NULL,
	[createdon] [datetime] DEFAULT GETDATE() NOT NULL	
)

-------------------------------------------------------------------------------------------
IF EXISTS (SELECT 1 FROM  sys.indexes WHERE object_id  = OBJECT_ID('people') AND name = 'idx_people_01')
PRINT 'CONSTRAINT idx_people_01 ALREADY EXISTS...ALTER TABLE ABORTED'
ELSE
CREATE UNIQUE  NONCLUSTERED INDEX [idx_people_01] ON [dbo].[people]
(
	[entity] ASC,
	[entityserno] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
-------------------------------------------------------------------------------------------
-------------------------------------------------------------------------------------------
PRINT 'Created: people'
-------------------------------------------------------------------------------------------
GRANT SELECT, INSERT, DELETE, UPDATE ON people TO tims_private
GO
-------------------------------------------------------------------------------------------
IF EXISTS (SELECT 1 FROM  sys.sequences WHERE name = 'people_serno_seq')
    PRINT 'SEQUENCE tims.people_serno_seq ALREADY EXISTS...'
ELSE
CREATE SEQUENCE tims.people_serno_seq  
    START WITH 1  
    INCREMENT BY 1 ;  
GO 
PRINT 'Created: people_serno_seq SEQUENCE'
-------------------------------------------------------------------------------------------

PRINT 'Creating: trg_people_INSERT trigger'
DROP TRIGGER [dbo].[trg_people_INSERT] 
GO
CREATE  TRIGGER [dbo].[trg_people_INSERT] 
   ON  [dbo].[people]
   AFTER INSERT
AS 
BEGIN
SET NOCOUNT ON 
         --Insert audit info
      declare @rowserno bigint;
	  declare @tab varchar(30) = 'people';
	  declare @log varchar(30) = 'Inserted';
      
	  select @rowserno=serno from inserted;
	  	EXEC [dbo].[psp_insert_applog] @log,@tab,'rowserno',@rowserno,'',@rowserno	
	  	
 END
GO
PRINT 'Created: trg_people_INSERT trigger'
-----------------------------------------------------------------------------------------------
PRINT 'Creating: trg_people_UPDATE trigger'
DROP TRIGGER [dbo].[trg_people_UPDATE] 
GO
CREATE  TRIGGER [dbo].[trg_people_UPDATE] 
   ON  [dbo].[people]
   AFTER UPDATE
AS 
BEGIN
SET NOCOUNT ON 
         --Update audit info
      declare @rowserno bigint;
	  declare @tab varchar(30) = 'people';
	  declare @log varchar(30) = 'Updated';
	  declare @oldvalue varchar(500);
	  declare @newvalue varchar(500);

	  select @rowserno=serno from deleted;  

      IF EXISTS(SELECT 1  FROM deleted d inner join inserted i on i.serno=d.serno and d.type<>i.type) 
      	BEGIN
      		SELECT @oldvalue = d.type,@newvalue =i.type  FROM deleted d inner join inserted i on i.serno=d.serno and d.type<>i.type
      		EXEC [dbo].[psp_insert_applog] @log,@tab,'type',@rowserno,@oldvalue,@newvalue	
      	END

      IF EXISTS(SELECT 1  FROM deleted d inner join inserted i on i.serno=d.serno and d.entity<>i.entity) 
      	BEGIN
      		SELECT @oldvalue = d.entity,@newvalue =i.entity  FROM deleted d inner join inserted i on i.serno=d.serno and d.entity<>i.entity
      		EXEC [dbo].[psp_insert_applog] @log,@tab,'entity',@rowserno,@oldvalue,@newvalue	
      	END

      IF EXISTS(SELECT 1  FROM deleted d inner join inserted i on i.serno=d.serno and d.entityserno<>i.entityserno) 
      	BEGIN
      		SELECT @oldvalue = d.entityserno,@newvalue =i.entityserno  FROM deleted d inner join inserted i on i.serno=d.serno and d.entityserno<>i.entityserno
      		EXEC [dbo].[psp_insert_applog] @log,@tab,'entityserno',@rowserno,@oldvalue,@newvalue	
      	END

      IF EXISTS(SELECT 1  FROM deleted d inner join inserted i on i.serno=d.serno and d.name<>i.name) 
      	BEGIN
      		SELECT @oldvalue = d.name,@newvalue =i.status  FROM deleted d inner join inserted i on i.serno=d.serno and d.name<>i.name
      		EXEC [dbo].[psp_insert_applog] @log,@tab,'name',@rowserno,@oldvalue,@newvalue	
      	END

      IF EXISTS(SELECT 1  FROM deleted d inner join inserted i on i.serno=d.serno and d.mobile<>i.mobile) 
      	BEGIN
      		SELECT @oldvalue = d.mobile,@newvalue =i.mobile  FROM deleted d inner join inserted i on i.serno=d.serno and d.mobile<>i.mobile
      		EXEC [dbo].[psp_insert_applog] @log,@tab,'mobile',@rowserno,@oldvalue,@newvalue	
      	END

      IF EXISTS(SELECT 1  FROM deleted d inner join inserted i on i.serno=d.serno and d.email<>i.email) 
      	BEGIN
      		SELECT @oldvalue = d.email,@newvalue =i.email  FROM deleted d inner join inserted i on i.serno=d.serno and d.email<>i.email
      		EXEC [dbo].[psp_insert_applog] @log,@tab,'email',@rowserno,@oldvalue,@newvalue	
      	END

      IF EXISTS(SELECT 1  FROM deleted d inner join inserted i on i.serno=d.serno and d.status<>i.status) 
      	BEGIN
      		SELECT @oldvalue = d.status,@newvalue =i.status  FROM deleted d inner join inserted i on i.serno=d.serno and d.status<>i.status
      		EXEC [dbo].[psp_insert_applog] @log,@tab,'status',@rowserno,@oldvalue,@newvalue	
      	END

    
		
 END
GO
PRINT 'Created: trg_people_UPDATE trigger'

---------------------------------------------------------------------------------------------------------------------------------------------
PRINT 'Creating: trg_people_DELETE trigger'
DROP TRIGGER [dbo].[trg_people_DELETE] 
GO
CREATE  TRIGGER [dbo].[trg_people_DELETE] 
   ON  [dbo].[people]
   AFTER DELETE
AS 
BEGIN
SET NOCOUNT ON 
      --Delete audit info
      declare @rowserno bigint;
	  declare @tab varchar(30) = 'people';
	  declare @log varchar(30) = 'Deleted';
      
	  select @rowserno=serno from deleted;
	  	EXEC [dbo].[psp_insert_applog] @log,@tab,'rowserno',@rowserno,@rowserno,''	  	
 END
GO
PRINT 'Created: trg_people_DELETE trigger'
--------------------------------------------------------------------------------------------------------------------------------------------

