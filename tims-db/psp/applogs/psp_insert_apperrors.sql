

/*---------------------------------------------------------------------------*\
 *																			 
 * InterSwitch Ltd.															 
 * Kenya																	 
 * 			
 *
 * File Name:		psp_insert_apperror.sql												  
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



IF EXISTS (SELECT * FROM sysobjects WHERE id = object_id('psp_insert_apperror'))
BEGIN
    PRINT 'Dropping current psp_insert_apperror'
   	DROP PROCEDURE psp_insert_apperror
END
GO

CREATE PROCEDURE [dbo].[psp_insert_apperror]
(
	@ERROR_PROCEDURE nvarchar(128),
	@ERROR_SEVERITY nvarchar(128),
	@ERROR_STATE nvarchar(128),	
	@ERROR_NUMBER nvarchar(128),	
	@ERROR_LINE nvarchar(128),
	@ERROR_MESSAGE nvarchar(4000)
)
AS

BEGIN    

	DECLARE  @userName varchar(30) = ISNULL(CAST(CONTEXT_INFO() as varchar(30)),SUSER_SNAME());
	DECLARE @errorserno bigint =  NEXT VALUE FOR tims.apperrors_serno_seq
	BEGIN TRY
		INSERT INTO apperrors VALUES (@errorserno,@userName,ERROR_PROCEDURE(),ERROR_SEVERITY(),ERROR_STATE(),ERROR_NUMBER(),ERROR_LINE(),ERROR_MESSAGE(),GETDATE());
	END TRY
	BEGIN CATCH 
		
	END CATCH    
END

GO

PRINT 'Created: psp_insert_apperror'

--------------------------------------------------------------------------------
GRANT  EXECUTE  ON dbo.psp_insert_apperror TO tims_private
GO

-----------------------------------------------------------------------------------------------
