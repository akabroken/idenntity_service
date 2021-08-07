
/*---------------------------------------------------------------------------*\
 *																			 
 * InterSwitch Ltd.															 
 * Kenya																	 
 * 			
 *
 * File Name:		tims-app-data.sql												  
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
 *  1		Linus			08/04/2019      			Created
 *
 *
 *	Last modified in: v1.0.0
\*---------------------------------------------------------------------------*/

USE [tims]
GO
-------------------------------------------------------------------------------

DECLARE @suppserno bigint = 1;

PRINT 'Inserting supplier'
EXEC [dbo].[psp_insert_supplier] 'INTERSWITCH EAST AFRICA( K) LTD','Orbit Place, Parklands/Highridge, Chiromo Ln','-1.2677965782113276, 36.809496822741636','P051173645V';
---------------------------------------------------------------------------------------------------------
PRINT 'Inserting businesses'
EXEC [dbo].[psp_insert_business] 'Motorcycle Shops and Dealers','Retail Outlet'
EXEC [dbo].[psp_insert_business] 'Motor Vehicle Supplies and New Parts','Retail Outlet'
EXEC [dbo].[psp_insert_business] 'Grocery Stores, Supermarkets','Retail Outlet'
---------------------------------------------------------------------------------------------------------
PRINT 'Inserting traders'
EXEC [dbo].[psp_insert_trader] 1,1,'Amahaya and Sons','P051173646V','Athi River,Machakos','-1.461139466498607, 36.975910549730955'
EXEC [dbo].[psp_insert_trader] 1,1,'David Supermarket','P051173647V','Kasarani,Nairobi','-1.2173101349629702, 36.8971773684757'
---------------------------------------------------------------------------------------------------------
PRINT 'Inserting termianls'

EXEC [dbo].[psp_insert_terminal] 1,1,'POS','KRAMW901202107007891','D','Amahaya and Sons -Athi River';
EXEC [dbo].[psp_insert_terminal] 1,2,'VIRTUAL','KRAMW901202107007900','D','David Supermarket -Kasarani';
