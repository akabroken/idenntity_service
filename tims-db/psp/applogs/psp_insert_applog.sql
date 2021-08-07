

/*---------------------------------------------------------------------------*\
 *																			 
 * InterSwitch Ltd.															 
 * Kenya																	 
 * 			
 *
 * File Name:		psp_insert_applog.sql												  
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
USE [tims]
GO



IF EXISTS (SELECT * FROM sysobjects WHERE id = object_id('psp_insert_applog'))
BEGIN
    PRINT 'Dropping current psp_insert_applog'
   	DROP PROCEDURE psp_insert_applog
END
GO

CREATE PROCEDURE [dbo].[psp_insert_applog]
(
	@log varchar(30),
	@tab varchar(30),
	@col varchar(30),	
	@rowserno bigint,	
	@oldvalue varchar(MAX),	
	@newvalue varchar(MAX)		
)
AS

BEGIN    

	BEGIN TRY
		DECLARE  @userName varchar(100) = ISNULL(CAST(CONTEXT_INFO() as varchar(100)),SUSER_SNAME());

		DECLARE @serno bigint=  NEXT VALUE FOR tims.applogs_serno_seq;

 		INSERT INTO dbo.applogs([serno],[username],[log],[tab],[col],[rowserno],[oldvalue],[newvalue],[host],[dbuser])
			VALUES(@serno,@userName,@log,@tab,@col,@rowserno,@oldvalue,@newvalue,HOST_NAME(),SUSER_SNAME())

	END TRY
	BEGIN CATCH 
		EXEC psp_insert_apperror ERROR_PROCEDURE,ERROR_SEVERITY,ERROR_STATE,ERROR_NUMBER,ERROR_LINE,ERROR_MESSAGE
	END CATCH   
END

GO

PRINT 'Created: psp_insert_applog'

--------------------------------------------------------------------------------
GRANT  EXECUTE  ON dbo.psp_insert_applog TO tims_private
GO

-----------------------------------------------------------------------------------------------
