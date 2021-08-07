


/*---------------------------------------------------------------------------*\
 *																			 
 * InterSwitch Ltd.															 
 * Kenya																	 
 * 			
 *
 * File Name:		psp_fetch_trader_bypin.sql												  
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

IF EXISTS (SELECT * FROM sysobjects WHERE id = object_id('psp_fetch_trader_bypin'))
BEGIN
    PRINT 'Dropping current psp_fetch_trader_bypin'
   	DROP PROCEDURE psp_fetch_trader_bypin
END
GO

CREATE PROCEDURE [dbo].[psp_fetch_trader_bypin]
(
	@pin varchar(50)	
)
AS

BEGIN
		SELECT T.*,b.name businesstype FROM traders t,businesses b WHERE b.serno = t.busserno AND pin = @pin  
END		  
GO

PRINT 'Created: psp_fetch_trader_bypin'

--------------------------------------------------------------------------------
GRANT  EXECUTE  ON dbo.psp_fetch_trader_bypin TO tims_private
GO

-----------------------------------------------------------------------------------------------
