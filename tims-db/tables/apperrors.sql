

/*---------------------------------------------------------------------------*\
 *																			 
 * InterSwitch Ltd.															 
 * Kenya																	 
 * 			
 *
 * File Name:		apperrors.sql												  
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
 *  1		Linus			07/05/2021      			Created
 *
 *
 *	Last modified in: v1.0.0
\*---------------------------------------------------------------------------*/

USE tims
GO

--------------------------------------------------------------------------------
PRINT ''
PRINT 'Creating Table: apperrors'
PRINT ''
--------------------------------------------------------------------------------
GO

IF EXISTS (SELECT 1 FROM dbo.sysobjects WHERE id = object_id(N'dbo.apperrors')
AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
PRINT 'TABLE apperrors ALREADY EXISTS... CREATE TABLE ABORTED'
ELSE
CREATE TABLE [dbo].[apperrors](
	[serno] [bigint] PRIMARY KEY,
	[username] [varchar](100) NULL,
	[errorprocedure] nvarchar(128),
	[errorseverity] [int],
	[errorstate] [int],
	[errornumber] [int],		
	[errorline] [int],		
	[errormessage]  nvarchar(4000),		
	[createdon] [datetime] DEFAULT GETDATE() NOT NULL
)
--------------------------------------------------------------------------------
-------------------------------------------------------------------------------------------
PRINT 'Created: apperrors'
-------------------------------------------------------------------------------------------
GRANT SELECT, INSERT, DELETE, UPDATE ON apperrors TO tims_private
GO
-------------------------------------------------------------------------------------------
IF EXISTS (SELECT 1 FROM  sys.sequences WHERE name = 'apperrors_serno_seq')
    PRINT 'SEQUENCE tims.apperrors_serno_seq ALREADY EXISTS...'
ELSE
CREATE SEQUENCE tims.apperrors_serno_seq  
    START WITH 1  
    INCREMENT BY 1 ;  
GO 
PRINT 'Created: apperrors_serno_seq SEQUENCE'
-------------------------------------------------------------------------------------------