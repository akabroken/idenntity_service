


/*---------------------------------------------------------------------------*\
 *																			 
 * InterSwitch Ltd.															 
 * Kenya																	 
 * 			
 *
 * File Name:		psp_insert_institution.sql												  
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
USE [identity]
GO

IF EXISTS (SELECT * FROM sysobjects WHERE id = object_id('psp_insert_institution'))
BEGIN
    PRINT 'Dropping current psp_insert_institution'
   	DROP PROCEDURE psp_insert_institution
END
GO

CREATE PROCEDURE [dbo].[psp_insert_institution]
(
	@name varchar(50)
)
AS
	
BEGIN

		IF EXISTS(SELECT serno FROM insitutions WHERE name=@name)
		 	BEGIN
				DECLARE @serno bigint SET = NEXT VALUE FOR identity.institutions_serno_seq

				INSERT INTO institutions(serno,name) VALUES (@serno,@name);

 			END			 	

END		  
GO

PRINT 'Created: psp_insert_institution'

--------------------------------------------------------------------------------
GRANT  EXECUTE  ON dbo.psp_insert_institution TO identity_private
GO

-----------------------------------------------------------------------------------------------
