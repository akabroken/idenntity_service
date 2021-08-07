

/*---------------------------------------------------------------------------*\
 *																			 
 * InterSwitch Ltd.															 
 * Kenya																	 
 * 			
 *
 * File Name:		applogs.sql												  
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
PRINT 'Creating Table: applogs'
PRINT ''
--------------------------------------------------------------------------------
GO

IF EXISTS (SELECT 1 FROM dbo.sysobjects WHERE id = object_id(N'dbo.applogs')
AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
PRINT 'TABLE applogs ALREADY EXISTS... CREATE TABLE ABORTED'
ELSE
CREATE TABLE [dbo].[applogs](
	[serno] [bigint] NOT NULL PRIMARY KEY,
	[username] [varchar](50) NOT NULL,
	[log] [varchar](50) NOT NULL,	
	[tab] [varchar](50) NOT NULL,	
	[col] [varchar](50) NOT NULL,	
	[rowserno][bigint] NOT NULL,
	[oldvalue] [varchar](3000) NOT NULL,
	[newvalue] [varchar](3000) NOT NULL,	
	[host] [varchar](50) NOT NULL,	
	[dbuser] [varchar](50) NOT NULL,		
	[createdon] [datetime] DEFAULT GETDATE() NOT NULL
)
-------------------------------------------------------------------------------------------
IF EXISTS (SELECT 1 FROM  sys.indexes WHERE object_id  = OBJECT_ID('applogs') AND name = 'idx_applogs_01')
PRINT 'CONSTRAINT idx_applogs_01 ALREADY EXISTS...ALTER TABLE ABORTED'
ELSE
CREATE  NONCLUSTERED INDEX [idx_applogs_01] ON [dbo].[applogs]
(
	[rowserno] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
-------------------------------------------------------------------------------------------
PRINT 'Created: applogs'
-------------------------------------------------------------------------------------------
GRANT SELECT, INSERT, DELETE, UPDATE ON applogs TO tims_private
GO
-------------------------------------------------------------------------------------------
IF EXISTS (SELECT 1 FROM  sys.sequences WHERE name = 'applogs_serno_seq')
    PRINT 'SEQUENCE tims.applogs_serno_seq ALREADY EXISTS...'
ELSE
CREATE SEQUENCE tims.applogs_serno_seq  
    START WITH 1  
    INCREMENT BY 1 ;  
GO 
PRINT 'Created: applogs_serno_seq SEQUENCE'
-------------------------------------------------------------------------------------------