


/*---------------------------------------------------------------------------*\
 *																			 
 * InterSwitch Ltd.															 
 * Kenya																	 
 * 			
 *
 * File Name:		psp_insert_trader.sql												  
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

IF EXISTS (SELECT * FROM sysobjects WHERE id = object_id('psp_insert_trader'))
BEGIN
    PRINT 'Dropping current psp_insert_trader'
   	DROP PROCEDURE psp_insert_trader
END
GO

CREATE PROCEDURE [dbo].[psp_insert_trader]
(
	@suppserno bigint,
	@busserno bigint,	
	@name varchar(50),	
	@pin varchar(50),	
	@location varchar(100),
	@coordinates varchar(100)	
)
AS

BEGIN
		DECLARE @serno bigint

		SET @serno =  NEXT VALUE FOR tims.traders_serno_seq
					
		INSERT INTO traders(serno,suppserno,busserno,name,location,coordinates,pin)
			VALUES(@serno,@suppserno,@busserno,@name,@location,@coordinates,@pin);	

		RETURN 0;
END		  
GO

PRINT 'Created: psp_insert_trader'

--------------------------------------------------------------------------------
GRANT  EXECUTE  ON dbo.psp_insert_trader TO tims_private
GO

-----------------------------------------------------------------------------------------------
