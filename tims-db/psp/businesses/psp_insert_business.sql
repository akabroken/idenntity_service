


/*---------------------------------------------------------------------------*\
 *																			 
 * InterSwitch Ltd.															 
 * Kenya																	 
 * 			
 *
 * File Name:		psp_insert_business.sql												  
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

IF EXISTS (SELECT * FROM sysobjects WHERE id = object_id('psp_insert_business'))
BEGIN
    PRINT 'Dropping current psp_insert_business'
   	DROP PROCEDURE psp_insert_business
END
GO

CREATE PROCEDURE [dbo].[psp_insert_business]
(
	@name varchar(200),
	@service varchar(200)			
)
AS

BEGIN
		DECLARE @serno bigint

		SET @serno =  NEXT VALUE FOR tims.businesses_serno_seq
					
		INSERT INTO businesses(serno,name,service)
			VALUES(@serno,@name,@service);	

		RETURN 0;
END		  
GO

PRINT 'Created: psp_insert_business'

--------------------------------------------------------------------------------
GRANT  EXECUTE  ON dbo.psp_insert_business TO tims_private
GO

-----------------------------------------------------------------------------------------------
