
/*---------------------------------------------------------------------------*\
 *																			 
 * InterSwitch Ltd.															 
 * Kenya																	 
 * 			
 *
 * File Name:		psp_fetch_banks.sql												  
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
 *  1		Linus			26/06/2019      			Created
 *
 *
 *	Last modified in: v1.0.0
\*---------------------------------------------------------------------------*/

USE [moba]
GO

IF EXISTS (SELECT * FROM sysobjects WHERE id = object_id('psp_fetch_banks'))
BEGIN
    PRINT 'Dropping current psp_fetch_banks'
   	DROP PROCEDURE psp_fetch_banks
END
GO

CREATE PROCEDURE [dbo].[psp_fetch_banks]
(
	@bankserno bigint
)
AS

BEGIN 

				SELECT serno,bankcode,bankname FROM tbl_banks WHERE bankserno = @bankserno AND status = 'A'
				
END

GO

PRINT 'Created: psp_fetch_banks'

--------------------------------------------------------------------------------
GRANT  EXECUTE  ON dbo.psp_fetch_banks TO moba_private
GO

-----------------------------------------------------------------------------------------------
