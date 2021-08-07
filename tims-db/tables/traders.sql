

/*---------------------------------------------------------------------------*\
 *																			 
 * InterSwitch Ltd.															 
 * Kenya																	 
 * 			
 *
 * File Name:		traders.sql												  
 * Version Number:	1												 
 *
 * 
 *
 * Copyright (C) 2019                              							 
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
PRINT 'Creating Table: traders'
PRINT ''
--------------------------------------------------------------------------------
GO

IF EXISTS (SELECT 1 FROM dbo.sysobjects WHERE id = object_id(N'dbo.traders')
AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
PRINT 'TABLE traders ALREADY EXISTS... CREATE TABLE ABORTED'
ELSE
CREATE TABLE [dbo].[traders](
	[serno] [bigint] NOT NULL PRIMARY KEY,
	[suppserno] [bigint] NOT NULL FOREIGN KEY REFERENCES SUPPLIERS(SERNO),
	[busserno] [bigint] NOT NULL FOREIGN KEY REFERENCES BUSINESSES(SERNO),
	[name] [varchar](50) NOT NULL,	
	[pin] [varchar](50) NOT NULL,
	[location] [varchar](100) NOT NULL,
	[coordinates] [varchar](100) NULL,	
	[status] [varchar](10) DEFAULT 'NORM' NOT NULL,
	[createdon] [datetime] DEFAULT GETDATE() NOT NULL	
)
-------------------------------------------------------------------------------------------
IF EXISTS (SELECT 1 FROM  sys.indexes WHERE object_id  = OBJECT_ID('traders') AND name = 'idx_traders_01')
PRINT 'CONSTRAINT idx_traders_01 ALREADY EXISTS...ALTER TABLE ABORTED'
ELSE
CREATE UNIQUE NONCLUSTERED INDEX [idx_traders_01] ON [dbo].[traders]
(
	[suppserno] ASC,
	[pin] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
-------------------------------------------------------------------------------------------
-------------------------------------------------------------------------------------------
PRINT 'Created: traders'
-------------------------------------------------------------------------------------------
GRANT SELECT, INSERT, DELETE, UPDATE ON traders TO tims_private
GO
-------------------------------------------------------------------------------------------
IF EXISTS (SELECT 1 FROM  sys.sequences WHERE name = 'traders_serno_seq')
    PRINT 'SEQUENCE tims.traders_serno_seq ALREADY EXISTS...'
ELSE
CREATE SEQUENCE tims.traders_serno_seq  
    START WITH 1  
    INCREMENT BY 1 ;  
GO 
PRINT 'Created: traders_serno_seq SEQUENCE'
-------------------------------------------------------------------------------------------

PRINT 'Creating: trg_traders_INSERT trigger'
DROP TRIGGER [dbo].[trg_traders_INSERT] 
GO
CREATE  TRIGGER [dbo].[trg_traders_INSERT] 
   ON  [dbo].[traders]
   AFTER INSERT
AS 
BEGIN
SET NOCOUNT ON 
         --Insert audit info
      declare @rowserno bigint;
	  declare @tab varchar(30) = 'traders';
	  declare @log varchar(30) = 'Inserted';
      
	  select @rowserno=serno from inserted;
	  	EXEC [dbo].[psp_insert_applog] @log,@tab,'rowserno',@rowserno,'',@rowserno	
	  	
 END
GO
PRINT 'Created: trg_traders_INSERT trigger'
-----------------------------------------------------------------------------------------------
PRINT 'Creating: trg_traders_UPDATE trigger'
DROP TRIGGER [dbo].[trg_traders_UPDATE] 
GO
CREATE  TRIGGER [dbo].[trg_traders_UPDATE] 
   ON  [dbo].[traders]
   AFTER UPDATE
AS 
BEGIN
SET NOCOUNT ON 
         --Update audit info
      declare @rowserno bigint;
	  declare @tab varchar(30) = 'traders';
	  declare @log varchar(30) = 'Updated';
	  declare @oldvalue varchar(500);
	  declare @newvalue varchar(500);

	  select @rowserno=serno from deleted;  

      IF EXISTS(SELECT 1  FROM deleted d inner join inserted i on i.serno=d.serno and d.name<>i.name) 
      	BEGIN
      		SELECT @oldvalue = d.name,@newvalue =i.status  FROM deleted d inner join inserted i on i.serno=d.serno and d.name<>i.name
      		EXEC [dbo].[psp_insert_applog] @log,@tab,'name',@rowserno,@oldvalue,@newvalue	
      	END

      IF EXISTS(SELECT 1  FROM deleted d inner join inserted i on i.serno=d.serno and d.busserno<>i.busserno) 
      	BEGIN
      		SELECT @oldvalue = d.busserno,@newvalue =i.status  FROM deleted d inner join inserted i on i.serno=d.serno and d.busserno<>i.busserno
      		EXEC [dbo].[psp_insert_applog] @log,@tab,'busserno',@rowserno,@oldvalue,@newvalue	
      	END

      IF EXISTS(SELECT 1  FROM deleted d inner join inserted i on i.serno=d.serno and d.pin<>i.pin) 
      	BEGIN
      		SELECT @oldvalue = d.pin,@newvalue =i.pin  FROM deleted d inner join inserted i on i.serno=d.serno and d.pin<>i.pin
      		EXEC [dbo].[psp_insert_applog] @log,@tab,'pin',@rowserno,@oldvalue,@newvalue	
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
     		
 END
GO
PRINT 'Created: trg_traders_UPDATE trigger'

---------------------------------------------------------------------------------------------------------------------------------------------
PRINT 'Creating: trg_traders_DELETE trigger'
DROP TRIGGER [dbo].[trg_traders_DELETE] 
GO
CREATE  TRIGGER [dbo].[trg_traders_DELETE] 
   ON  [dbo].[traders]
   AFTER DELETE
AS 
BEGIN
SET NOCOUNT ON 
      --Delete audit info
      declare @rowserno bigint;
	  declare @tab varchar(30) = 'traders';
	  declare @log varchar(30) = 'Deleted';
      
	  select @rowserno=serno from deleted;
	  	EXEC [dbo].[psp_insert_applog] @log,@tab,'rowserno',@rowserno,@rowserno,''	  	
 END
GO
PRINT 'Created: trg_traders_DELETE trigger'
--------------------------------------------------------------------------------------------------------------------------------------------

