


/*---------------------------------------------------------------------------*\
 *																			 
 * InterSwitch Ltd.															 
 * Kenya																	 
 * 			
 *
 * File Name:		psp_insert_terminal.sql												  
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

IF EXISTS (SELECT * FROM sysobjects WHERE id = object_id('psp_insert_terminal'))
BEGIN
    PRINT 'Dropping current psp_insert_terminal'
   	DROP PROCEDURE psp_insert_terminal
END
GO

CREATE PROCEDURE [dbo].[psp_insert_terminal]
(
	@suppserno bigint,
	@tradserno bigint,	
	@type varchar(10),	
	@msn varchar(50),	
	@msntype varchar(1),
	@name varchar(100)	
)
AS

BEGIN
		DECLARE @serno bigint

		SET @serno =  NEXT VALUE FOR tims.terminals_serno_seq
					
		INSERT INTO terminals(serno,suppserno,tradserno,type,msn,msntype,name)
			VALUES(@serno,@suppserno,@tradserno,@type,@msn,@msntype,@name);	

		RETURN 0;
END		  
GO

PRINT 'Created: psp_insert_terminal'

--------------------------------------------------------------------------------
GRANT  EXECUTE  ON dbo.psp_insert_terminal TO tims_private
GO

-----------------------------------------------------------------------------------------------
