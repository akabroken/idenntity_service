


/*---------------------------------------------------------------------------*\
 *																			 
 * InterSwitch Ltd.															 
 * Kenya																	 
 * 			
 *
 * File Name:		psp_fetch_terminal_bymsn.sql												  
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

IF EXISTS (SELECT * FROM sysobjects WHERE id = object_id('psp_fetch_terminal_bymsn'))
BEGIN
    PRINT 'Dropping current psp_fetch_terminal_bymsn'
   	DROP PROCEDURE psp_fetch_terminal_bymsn
END
GO

CREATE PROCEDURE [dbo].[psp_fetch_terminal_bymsn]
(
	@msn varchar(50)	
)
AS

BEGIN
		SELECT s.pin,msn,msntype,COALESCE(lastinvoicenumber,'') lastinvoicenumber,COALESCE(nextinvoicenumber,'') nextinvoicenumber,rsapubkey,t.name,t.location,t.coordinates FROM terminals t,suppliers s WHERE s.serno = t.suppserno AND msn = @msn
END		  
GO

PRINT 'Created: psp_fetch_terminal_bymsn'

--------------------------------------------------------------------------------
GRANT  EXECUTE  ON dbo.psp_fetch_terminal_bymsn TO tims_private
GO

-----------------------------------------------------------------------------------------------
